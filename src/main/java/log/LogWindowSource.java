package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;


public class LogWindowSource {
    private int m_iQueueLength;

    private final ConcurrentLinkedQueue<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ConcurrentLinkedQueue<>();
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);

        synchronized (m_messages){
            addNewMessage(entry);
        }

        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        if (activeListeners != null) {
            for (LogChangeListener listener : activeListeners) {
                listener.onLogChanged();
            }
        }
    }

    private void addNewMessage(LogEntry message) {
        if (size() == m_iQueueLength){
            m_messages.poll();
        }
        m_messages.add(message);
    }

    private int size() {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        CopyOnWriteArrayList<LogEntry> messages = new CopyOnWriteArrayList<>();
        int i = 0;
        for (LogEntry entry : m_messages) {
            if (i >= startFrom && i <= indexTo) {
                messages.add(entry);
                i++;
            }
        }
        return messages;
    }

    public Iterable<LogEntry> all() {
        return m_messages;
    }
}
