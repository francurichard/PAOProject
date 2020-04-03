import java.util.UUID;

public class RequestResponse {
    private String uuid;
    private boolean result;

    public RequestResponse(String uuid, boolean result) {
        this.uuid = uuid;
        this.result = result;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isResult() {
        return result;
    }
}
