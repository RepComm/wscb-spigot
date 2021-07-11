package repcomm;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WSServer extends WebSocketServer {
  private List<WSEventListener> listeners = new ArrayList<>();

  private ConcurrentLinkedDeque<WSEvent> unconsumedEvents = new ConcurrentLinkedDeque<WSEvent>();

  private final ReentrantLock listenerLock = new ReentrantLock();
  
  public WSServer(int port) {
    super(new InetSocketAddress(port));    
  }

  public static WSServer fromPort (int port) {
    return new WSServer(port);
  }

  public WSServer listen (WSEventListener listener) {
    this.listenerLock.lock();
    this.listeners.add(listener);
    this.listenerLock.unlock();
    return this;
  }

  public WSEvent createEvent (String type) {
    return new WSEvent(type);
  }

  /**Loops through unconsumed events and calls dispatchEvent on each one, consuming it
   * 
   * This implementation utilizes ConcurrentLinkedDeque, which is thread safe.
   * You may run this method from another thread, it will be the thread the WSEventListeners are executed in.
   * @return
   */
  public WSServer pollEvents () {
    WSEvent evt;

    while ( (evt = this.unconsumedEvents.pollFirst()) != null) {
      this.dispatchEvent(evt);
    }

    return this;
  }

  /**Adds the event to unconsumedEvents, called internally
   * @param evt
   * @return
   */
  public WSServer pushEvent (WSEvent evt) {
    this.unconsumedEvents.addLast(evt);
    return this;
  }

  /**Calls every event listener, passing each the event
   * 
   * Be sure to call this from whatever thread you intend your event listeners to be executed from
   * 
   * @param evt
   * @return
   */
  public WSServer dispatchEvent (WSEvent evt) {
    this.listenerLock.lock();

    for (WSEventListener listener : this.listeners) {
      listener.onEvent(evt);
    }

    this.listenerLock.unlock();
    return this;
  }

  public WSServer deafen (WSEventListener listener) {
    this.listenerLock.lock();
    this.listeners.remove(listener);
    this.listenerLock.unlock();
    return this;
  }

  //----This method is called by Java-WebSocket when a client connects
  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {

    //----Here we create an event object and thread-safely push it onto a queue
    //----It will be consumed by event listeners during pollEvents()
    WSEvent evt = this.createEvent(WSEvent.EVENT_TYPE_CONNECT);
    evt.client = conn;
    evt.handshake = handshake;
    this.pushEvent(evt);
  }

  //----This method is called by Java-WebSocket when a client disconnects
  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    
    //----Here we create an event object and thread-safely push it onto a queue
    //----It will be consumed by event listeners during pollEvents()
    WSEvent evt = this.createEvent(WSEvent.EVENT_TYPE_DISCONNECT);
    evt.client = conn;
    evt.disconnectReason = reason;
    evt.disconnectCode = code;
    evt.disconnectWasRemoteClient = remote;
    this.pushEvent(evt);
  }

  //----This method is called by Java-WebSocket when a client sends a string message
  @Override
  public void onMessage(WebSocket conn, String message) {

    //----Here we create an event object and thread-safely push it onto a queue
    //----It will be consumed by event listeners during pollEvents()
    WSEvent evt = this.createEvent(WSEvent.EVENT_TYPE_MESSAGE_STRING);
    evt.client = conn;
    evt.messageString = message;
    this.pushEvent(evt);
  }

  //----This method is called by Java-WebSocket when a client produces an exception
  @Override
  public void onError(WebSocket conn, Exception ex) {

    //----Here we create an event object and thread-safely push it onto a queue
    //----It will be consumed by event listeners during pollEvents()
    WSEvent evt = this.createEvent(WSEvent.EVENT_TYPE_EXCEPTION);
    evt.client = conn;
    evt.exception = ex;
    this.pushEvent(evt);
  }

  //----This method is called by Java-WebSocket when the server starts
  @Override
  public void onStart() {

    //----Here we create an event object and thread-safely push it onto a queue
    //----It will be consumed by event listeners during pollEvents()
    WSEvent evt = this.createEvent(WSEvent.EVENT_TYPE_START);
    this.pushEvent(evt);
  }

  //----Apparently Java-WebSocket doesn't have onStop, so we override the stop method to add that in
  public void stop(int timeout) throws InterruptedException {
    super.stop(timeout);
    this.onStop();
  }

  public void onStop () {
    //----Here we create an event object and thread-safely push it onto a queue
    //----It will be consumed by event listeners during pollEvents()
    WSEvent evt = this.createEvent(WSEvent.EVENT_TYPE_STOP);
    this.pushEvent(evt);
  }
}
