package mx.unam.ciencias.edd.test;

import java.util.Random;
import mx.unam.ciencias.edd.Dispersores;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Dispersores}.
 */
public class TestDispersores {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Arreglo de bytes aleatorios. */
    private static final byte[] ARREGLO = {
            (byte)0x33, (byte)0xf0, (byte)0x28, (byte)0x9e,
            (byte)0xb3, (byte)0x35, (byte)0x40, (byte)0xbf,
            (byte)0x51, (byte)0x28, (byte)0xc1, (byte)0xf1,
            (byte)0x82, (byte)0xad, (byte)0xf3, (byte)0x63,
            (byte)0xf6, (byte)0xc4, (byte)0x0a, (byte)0x8e,
            (byte)0xab, (byte)0x4f, (byte)0x09, (byte)0xf5,
            (byte)0x08, (byte)0xba, (byte)0xe2, (byte)0xbe,
            (byte)0x1c, (byte)0xb0, (byte)0xbf, (byte)0xe2,
            (byte)0xf7, (byte)0x9e, (byte)0xdf, (byte)0xc8,
            (byte)0x13, (byte)0x00, (byte)0x93, (byte)0xa6,
            (byte)0x84, (byte)0xf3, (byte)0x08, (byte)0xc2,
            (byte)0xb1, (byte)0xd1, (byte)0xb1, (byte)0xb2,
            (byte)0x62, (byte)0xee, (byte)0x8f, (byte)0x68,
            (byte)0x7e, (byte)0xce, (byte)0x85, (byte)0xd2,
            (byte)0x45, (byte)0x60, (byte)0x1d, (byte)0xd2,
            (byte)0xba, (byte)0xdb, (byte)0x4b, (byte)0x03,
            (byte)0xde, (byte)0x55, (byte)0xe7, (byte)0x8b,
            (byte)0x8e, (byte)0x27, (byte)0xed, (byte)0xc4,
            (byte)0xbe, (byte)0x4f, (byte)0xb8, (byte)0x43,
            (byte)0x73, (byte)0xe9, (byte)0x43, (byte)0xef,
            (byte)0x28, (byte)0x1a, (byte)0x77, (byte)0x71,
            (byte)0x1c, (byte)0x3d, (byte)0xae, (byte)0x72,
            (byte)0xd6, (byte)0xb3, (byte)0x7c, (byte)0xbd,
            (byte)0x5e, (byte)0xa1, (byte)0x5c, (byte)0x7d,
            (byte)0x92, (byte)0x83, (byte)0x2b, (byte)0x97
        };

    /* Los resultados XOR para los subarreglos. */
    private static final int[] RESULTADOS_XOR = {
        0x33000000, 0x33f00000, 0x33f02800, 0x33f0289e, 0x80f0289e,
        0x80c5289e, 0x80c5689e, 0x80c56821, 0xd1c56821, 0xd1ed6821,
        0xd1eda921, 0xd1eda9d0, 0x53eda9d0, 0x5340a9d0, 0x53405ad0,
        0x53405ab3, 0xa5405ab3, 0xa5845ab3, 0xa58450b3, 0xa584503d,
        0x0e84503d, 0x0ecb503d, 0x0ecb593d, 0x0ecb59c8, 0x06cb59c8,
        0x067159c8, 0x0671bbc8, 0x0671bb76, 0x1a71bb76, 0x1ac1bb76,
        0x1ac10476, 0x1ac10494, 0xedc10494, 0xed5f0494, 0xed5fdb94,
        0xed5fdb5c, 0xfe5fdb5c, 0xfe5fdb5c, 0xfe5f485c, 0xfe5f48fa,
        0x7a5f48fa, 0x7aac48fa, 0x7aac40fa, 0x7aac4038, 0xcbac4038,
        0xcb7d4038, 0xcb7df138, 0xcb7df18a, 0xa97df18a, 0xa993f18a,
        0xa9937e8a, 0xa9937ee2, 0xd7937ee2, 0xd75d7ee2, 0xd75dfbe2,
        0xd75dfb30, 0x925dfb30, 0x923dfb30, 0x923de630, 0x923de6e2,
        0x283de6e2, 0x28e6e6e2, 0x28e6ade2, 0x28e6ade1, 0xf6e6ade1,
        0xf6b3ade1, 0xf6b34ae1, 0xf6b34a6a, 0x78b34a6a, 0x78944a6a,
        0x7894a76a, 0x7894a7ae, 0xc694a7ae, 0xc6dba7ae, 0xc6db1fae,
        0xc6db1fed, 0xb5db1fed, 0xb5321fed, 0xb5325ced, 0xb5325c02,
        0x9d325c02, 0x9d285c02, 0x9d282b02, 0x9d282b73, 0x81282b73,
        0x81152b73, 0x81158573, 0x81158501, 0x57158501, 0x57a68501,
        0x57a6f901, 0x57a6f9bc, 0x09a6f9bc, 0x0907f9bc, 0x0907a5bc,
        0x0907a5c1, 0x9b07a5c1, 0x9b84a5c1, 0x9b848ec1
    };

    /* Los resultados BJ para los subarreglos. */
    private static final int[] RESULTADOS_BJ = {
        0xdae7fd29, 0x3d0a3be6, 0xe202bddf, 0x5d31d3e5, 0x86a430e4,
        0x19a46638, 0xab950d9d, 0xc49edc87, 0x06c424cc, 0xdc2ac5af,
        0xa028061f, 0xe67840c4, 0x01d1f837, 0xc38d0033, 0x989b8561,
        0xc527e416, 0xaca44779, 0x68fc8252, 0x1dc92149, 0x43a58715,
        0x2d1b0358, 0x1a3a3e73, 0xa6767615, 0x53746416, 0xeee4e7d0,
        0x65d08a76, 0x2cb87b21, 0xf6ba8201, 0xa40bd730, 0x317fe33e,
        0x8694e504, 0xa57e86c2, 0x110f236c, 0x128e4b71, 0xd7d7a880,
        0x37fa32a4, 0xed3948e7, 0x369906be, 0xf24daa75, 0x5822a2c8,
        0xff5e3b4b, 0x61a47d5d, 0x7efc8d85, 0xc2a71951, 0x48a4779b,
        0xa3b06f44, 0xe543896e, 0x0df14dac, 0x4a9a7d9b, 0xd63f5ca4,
        0x7dacbd10, 0x086b3aab, 0x6fae7331, 0x02caec79, 0x17ae1bc8,
        0x8d414811, 0x997bdd82, 0x24bae168, 0xdb6698a3, 0xaa6eb511,
        0xcdfceeff, 0xe3d640ae, 0xf62560a2, 0xef52d532, 0xd0f380cd,
        0x99144b98, 0x3dfcddfd, 0xd2b7e2b8, 0x23d2e3b0, 0x25c8f169,
        0x3b6adb5b, 0x7f6e1234, 0x0f6c2b78, 0x4aca2ea7, 0x00f89d1a,
        0x354eaf22, 0x91a2a7e4, 0x5d4e9313, 0x897e8a8c, 0x3d61592a,
        0x6e11325e, 0xa0bdb6d5, 0x793f1fa6, 0x08c22efc, 0x6f4b9cb8,
        0xf0a4fc1a, 0x0375ca33, 0x3e6e3ff4, 0x1278cc80, 0xba444dcf,
        0x773a5317, 0x979807bd, 0xd7411484, 0x0578a2c7, 0x3a876a31,
        0x083a9960, 0x63d67c30, 0x5f36699f, 0x309415d5
    };

    /* Los resultados DJB para los subarreglos. */
    private static final int[] RESULTADOS_DJB = {
        0x0002b5d8, 0x005971c8, 0x0b87aaf0, 0x7c7d098e, 0x0c1e3c01,
        0x8fe5bc56, 0x8c9d4756, 0x204632d5, 0x290c8dc6, 0x4a9e46ae,
        0x9e671d2f, 0x6b4ac400, 0xd4a34482, 0x690bd56f, 0x8a868442,
        0xdb570ce5, 0x4638aa7b, 0x0d4dfa9f, 0xb70d4e89, 0x98b72037,
        0xaf9b27c2, 0xa3002051, 0x03042a7a, 0x63897aaf, 0xd4b8d097,
        0x6bd2e431, 0xe62f6b33, 0xac1cd251, 0x2fb71c8d, 0x269aaedd,
        0xf9f08b3c, 0x3801f39e, 0x38406855, 0x404d7393, 0x49fbe6d2,
        0x8978c1da, 0xb890fd2d, 0xcab0a2cd, 0x20c4fd00, 0x39649da6,
        0x65f852ea, 0x2502b11d, 0xc558d4c5, 0x70736e27, 0x7ee133b8,
        0x5b07ab89, 0xbbfd1d5a, 0x3ba0c94c, 0xafb9f32e, 0xa6f859dc,
        0x860395eb, 0x467653b3, 0x1540ca91, 0xbd5a1d7f, 0x689dcde4,
        0x7c578b36, 0x0748f23b, 0xf06739fb, 0xfd4e7978, 0xa71da94a,
        0x8ad2d344, 0xe52d3c9f, 0x8ad4d0ca, 0xe56eea0d, 0x934c2c8b,
        0xfcd1be40, 0x97098727, 0x783a6c92, 0x7f87ff60, 0x7087eb87,
        0x81855d54, 0xb2310898, 0xf8521c56, 0x0295a765, 0x554a94bd,
        0xfe9d2ca0, 0xd242c113, 0x1a9ae45c, 0x6df7701f, 0x2ce574ee,
        0xc99412d6, 0xfc166db0, 0x7ee42427, 0x5b68a978, 0xc87dd894,
        0xd838eb51, 0xdf56561f, 0xca211a71, 0x0e446967, 0xd6d196fa,
        0xb10476b6, 0xd1934e33, 0x03fd14f1, 0x839fb3b2, 0xf7962a4e,
        0xea5b748b, 0x35ca067d, 0xef0ad6a0, 0xd065aacb
    };

    /* Regresa un subarreglo del arreglo. */
    private byte[] subArreglo(int n) {
        byte[] sub = new byte[n];
        for (int i = 0; i < n; i++)
            sub[i] = ARREGLO[i];
        return sub;
    }

    /**
     * Prueba unitaria para {@link Dispersores#dispersaXOR}.
     */
    @Test public void testDispersaXOR() {
        for (int i = 1; i < ARREGLO.length; i++) {
            byte[] sub = subArreglo(i);
            int r = Dispersores.dispersaXOR(sub);
            Assert.assertTrue(RESULTADOS_XOR[i-1] == r);
        }
    }

    /**
     * Prueba unitaria para {@link Dispersores#dispersaBJ}.
     */
    @Test public void testDispersaBJ() {
        for (int i = 1; i < ARREGLO.length; i++) {
            byte[] sub = subArreglo(i);
            int r = Dispersores.dispersaBJ(sub);
            Assert.assertTrue(RESULTADOS_BJ[i-1] == r);
        }
    }

    /**
     * Prueba unitaria para {@link Dispersores#dispersaDJB}.
     */
    @Test public void testDispersaDJB() {
        for (int i = 1; i < ARREGLO.length; i++) {
            byte[] sub = subArreglo(i);
            int r = Dispersores.dispersaDJB(sub);
            Assert.assertTrue(RESULTADOS_DJB[i-1] == r);
        }
    }
}
