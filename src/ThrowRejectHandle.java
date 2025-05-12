public class ThrowRejectHandle implements RejectHandle{
    @Override
    public void reject(Runnable rejectCommand,MythreadPool mythreadPool){
        throw new RuntimeException("阻塞队列满了");
    }
}
