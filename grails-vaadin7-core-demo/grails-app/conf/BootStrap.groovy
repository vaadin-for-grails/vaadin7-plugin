import demo.Role
import demo.User
import demo.UserRole

class BootStrap {

    def init = { servletContext ->
        def admins = Role.findOrCreateByAuthority("ROLE_ADMIN")
        admins.save(flush: true, failOnError: true)
        def admin = User.findOrCreateByUsernameAndPassword("admin", "admin")
        admin.save(flush: true, failOnError: true)
        UserRole.create(admin, admins, true)
    }
    def destroy = {
    }
}
