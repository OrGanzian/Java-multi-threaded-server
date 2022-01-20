package com.bbumgames.server;

/**
 * By implementing this interface, a list of all registers will be well maintained and updated
 * @param <T> type of the subscriber (Socket in the case of the TcpServer)
 */
public interface Publisher<T> {

    void register(T o);

    void unRegister(T o);

    void notifyAllSubscribers();

}
