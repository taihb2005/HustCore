package thread;

import javax.swing.plaf.SeparatorUI;
import java.util.concurrent.Semaphore;

public class LoadThread extends Thread{
    protected static Semaphore semaphore = new Semaphore(1);
}
