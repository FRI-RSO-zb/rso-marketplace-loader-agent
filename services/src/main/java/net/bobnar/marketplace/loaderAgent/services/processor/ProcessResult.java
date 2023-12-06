package net.bobnar.marketplace.loaderAgent.services.processor;

public abstract class ProcessResult {
    public String status;
    public String errors;

    public void fail() {
        this.status = "fail";
    }

    public void addError(String error) {
        this.fail(error);
    }

    public void fail(String errors) {
        this.status = "fail";
        if (this.errors != null && !this.errors.isEmpty()) {
            this.errors += "\n" + errors;
        } else {
            this.errors = errors;
        }
    }

    public void success() {
        this.status = "ok";
    }

    public boolean isSuccess() {
        return "ok".equals(this.status);
    }

    public boolean isFailed() {
        return !this.isSuccess();
    }
}
