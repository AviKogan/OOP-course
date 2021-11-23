import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite Test class, run LockerTest.class, LongTermTest.class and SpaceshipTest.class.
 *
 * @author Avi Kogan.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                LockerTest.class,
                LongTermTest.class,
                SpaceshipTest.class
        }
)
public class SpaceshipDepositoryTest {

}
