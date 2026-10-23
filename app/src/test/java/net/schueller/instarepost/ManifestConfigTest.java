package net.schueller.instarepost;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Test;

/**
 * Regressions for targetSdk 31+/scoped storage: the launcher activity must declare exported, and
 * legacy storage permissions are limited to API <= 28 (media lives in the app-specific dir).
 */
public class ManifestConfigTest {
    private static String manifest() throws Exception {
        File f = new File("src/main/AndroidManifest.xml");
        if (!f.exists()) f = new File("app/src/main/AndroidManifest.xml");
        return new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
    }

    @Test
    public void mainActivityIsExported() throws Exception {
        assertTrue(manifest().matches("(?s).*android:name=\"\\.activities\\.MainActivity\"\\s+android:exported=\"true\".*"));
    }

    @Test
    public void storagePermissionsOnlyOnLegacyDevices() throws Exception {
        String m = manifest();
        assertTrue(m.matches("(?s).*WRITE_EXTERNAL_STORAGE\"\\s+android:maxSdkVersion=\"28\".*"));
        assertTrue(m.matches("(?s).*READ_EXTERNAL_STORAGE\"\\s+android:maxSdkVersion=\"28\".*"));
    }
}
