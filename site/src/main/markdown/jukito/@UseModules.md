#@UseModules

`@UseModules` annotation can be used to inherit bindings from an already existing module. `@UseModules` an array of module classes and can be applied to a test class or a test methods. The only restriction is that the modules imported must not extends JukitoModule.

Here's an exemple that you can find under the Jukito test sources:

@RunWith(JukitoRunner.class)
@UseModules({AbcModule.class, DefModule.class})
public class UseModulesTest extends UseModulesTestBase {
    interface Abc {
    }

    interface Def {
    }

    interface Ghj {
    }

    interface Klm {
    }

    static class AbcImpl implements Abc {
    }

    static class DefImpl implements Def {
    }

    static class AbcImpl2 implements Abc {
    }

    static class DefImpl2 implements Def {
    }

    static class KlmImpl implements Klm {
    }

    @Test
    @UseModules(XyzModule.class)
    public void testInjectionUsingMethodModules(Abc abc, Def def) {
        assertTrue(abc instanceof AbcImpl2);
        assertTrue(def instanceof DefImpl2);
    }

    @Test
    public void testInjectionWithExternalModules(Abc abc, Def def, Klm klm) {
        assertTrue(abc instanceof AbcImpl);
        assertTrue(def instanceof DefImpl);
        assertTrue(klm instanceof KlmImpl);
    }

    @Test
    public void testAutoMockingForMissingBindings(Ghj ghj) {
        assertNotNull(ghj);
        assertTrue(Mockito.mockingDetails(ghj).isMock());
    }
}

class XyzModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Abc.class).to(UseModulesTest.AbcImpl2.class);
        bind(Def.class).to(UseModulesTest.DefImpl2.class);
    }
}

@UseModules({ DefModule.class, KlmModule.class })
abstract class UseModulesTestBase {
    // KlmModule should get installed
    // DefModule should be ignored because subClass has it
}

class AbcModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Abc.class).to(AbcImpl.class);
    }
}

class DefModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Def.class).to(DefImpl.class);
    }
}

class KlmModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Klm.class).to(KlmImpl.class);
    }
}
