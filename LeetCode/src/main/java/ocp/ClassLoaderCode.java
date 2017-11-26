package ocp;

public class ClassLoaderCode {

    public static void main(String[] args) {

        System.out.println("class loader for HashMap: " + java.util.HashMap.class.getClassLoader());
        System.out.println("class loader for DNSNameService: "
                + sun.net.spi.nameservice.dns.DNSNameService.class.getClassLoader());
        System.out.println("class loader for this class: " + ClassLoaderCode.class.getClassLoader());
        System.out.println(com.mysql.jdbc.Blob.class.getClassLoader());

        // class loader for HashMap: null
        // class loader for DNSNameService: sun.misc.Launcher$ExtClassLoader@3d4eac69
        // class loader for this class: sun.misc.Launcher$AppClassLoader@2a139a55
        // sun.misc.Launcher$AppClassLoader@2a139a55
    }
}
