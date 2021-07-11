package repcomm;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public class WSEvent {
  public static final String EVENT_TYPE_START = "start";
  public static final String EVENT_TYPE_DISCONNECT = "disconnect";
  public static final String EVENT_TYPE_CONNECT = "connect";
  public static final String EVENT_TYPE_MESSAGE_STRING = "message-string";
  public static final String EVENT_TYPE_MESSAGE_BUFFER = "message-buffer";
  public static final String EVENT_TYPE_EXCEPTION = "exception";
  public static final String EVENT_TYPE_STOP = "stop";

  public WSEvent (String type) {
    this.type = type;
  }

  /**One of EVENT_TYPE_ prefixed constant strings defined statically in WSEvent class*/
  public String type;

  /**The client involved in this event, if any
   * Populated for EVENT_TYPE_CONNECT, EVENT_TYPE_MESSAGE_STRING, EVENT_TYPE_MESSAGE_BUFFER, EVENT_TYPE_EXCEPTION
   */
  public WebSocket client;

  /**The client handshake involved in this event, if any
   * Populated for EVENT_TYPE_CONNECT
   */
  public ClientHandshake handshake;

  /**The message produced from the remote socket, if any
   * Populated for EVENT_TYPE_MESSAGE_STRING
   */
  public String messageString;

  /**The exception involved in this event, if any
   * Populated for EVENT_TYPE_EXCEPTION
   */
  public Exception exception;

  /**Stop code involved in this event, if any
   * Populated for EVENT_TYPE_STOP
   */
  public int disconnectCode;
  /**Stop reason involved in this event, if any
   * Populated for EVENT_TYPE_STOP
   */
  public String disconnectReason;
  /**Denotes if the client was remote during EVENT_TYPE_STOP event
   * Populated for EVENT_TYPE_STOP
   */
  public boolean disconnectWasRemoteClient;

}
