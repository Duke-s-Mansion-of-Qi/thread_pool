public class DiscardRejectHandle implements RejectHandle{
    @Override
    public void reject(Runnable rejectcommand,MythreadPool mythreadPool){
        mythreadPool.blockingQueue.poll();
        mythreadPool.execute(rejectcommand);
    }
}
