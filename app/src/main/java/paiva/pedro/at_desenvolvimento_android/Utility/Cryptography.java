package paiva.pedro.at_desenvolvimento_android.Utility;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.KeysetManager;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.proto.KeyTemplate;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

public class Cryptography {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String encrypt(String password) throws GeneralSecurityException {

        TinkConfig.register();

        KeyTemplate keyTemplate = AeadKeyTemplates.CHACHA20_POLY1305;
        KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplate);
        KeysetHandle rotatedKeysetHandle = KeysetManager
                .withKeysetHandle(keysetHandle)
                .rotate(keyTemplate)
                .getKeysetHandle();

        Aead aead = AeadFactory.getPrimitive(rotatedKeysetHandle);

        byte[] cipherPass = aead.encrypt(password.getBytes(StandardCharsets.UTF_8), null);
        byte[] encoded = Base64.encode(cipherPass, Base64.DEFAULT);

        String hashedPasswd = toHexString(encoded);

        return hashedPasswd;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String decrypt(String password) throws GeneralSecurityException {
        TinkConfig.register();

        KeyTemplate keyTemplate = AeadKeyTemplates.CHACHA20_POLY1305;
        KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplate);
        KeysetHandle rotatedKeysetHandle = KeysetManager
                .withKeysetHandle(keysetHandle)
                .rotate(keyTemplate)
                .getKeysetHandle();

        Aead aead = AeadFactory.getPrimitive(rotatedKeysetHandle);
        byte[] decoded = Base64.decode(password, Base64.DEFAULT);
        byte[] decryptedBytePass = aead.decrypt(decoded, null);

        String decryptPasswd = new String(decryptedBytePass);

        return decryptPasswd;
    }

    public static String toHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(toHex(bytes[i] >> 4));
            sb.append(toHex(bytes[i]));
        }

        return sb.toString();
    }

    private static char toHex(int nibble) {
        final char[] hexDigit =
                {
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
                };
        return hexDigit[nibble & 0xF];
    }
}
