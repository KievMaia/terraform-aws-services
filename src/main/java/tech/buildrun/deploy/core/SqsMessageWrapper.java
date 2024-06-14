package tech.buildrun.deploy.core;

import java.util.Objects;

public class SqsMessageWrapper {
    public String Message;

    public SqsMessageWrapper(final String message) {
        Message = message;
    }

    public SqsMessageWrapper() {
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(final String message) {
        Message = message;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SqsMessageWrapper that = (SqsMessageWrapper) o;
        return Objects.equals(Message, that.Message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Message);
    }

    @Override
    public String toString() {
        return "SqsMessageWrapper{" +
                "Message='" + Message + '\'' +
                '}';
    }
}