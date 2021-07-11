package repcomm;

public interface WSEventListener {
  public void onEvent (WSEvent evt);
  /**One of EVENT_TYPE_ prefixed constant strings defined statically in WSEvent class*/
}
