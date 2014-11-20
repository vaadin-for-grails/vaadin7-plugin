import grails.test.AbstractCliTestCase

class VaadinCreateThemeTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testVaadinCreateTheme() {

        execute(["vaadin-create-theme"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
