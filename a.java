import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import javax.management.ObjectName;

@RestController
public class HeapDumpController {

    @GetMapping("/heapdump")
    public String triggerHeapDump() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("com.sun.management:type=HotSpotDiagnostic");
            Object[] params = new Object[] { "heapdump.hprof", true };
            String[] signature = new String[] { String.class.getName(), boolean.class.getName() };
            server.invoke(name, "dumpHeap", params, signature);
            return "Heap dump created successfully.";
        } catch (Exception e) {
            return "Error during heap dump creation: " + e.getMessage();
        }
    }
}
