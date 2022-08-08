public enum Command {
    UPLOAD("./upload"),
    DOWNLOAD("./download"),
    CLOSE("./close");

    String command;
    Command(String command) {
        this.command=command;
    }
    public String getTitle() {
        return command;
    }
}
