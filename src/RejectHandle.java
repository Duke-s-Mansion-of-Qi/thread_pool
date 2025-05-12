public interface RejectHandle {
    void reject(Runnable rejectCommand,MythreadPool mythreadPool);
}
