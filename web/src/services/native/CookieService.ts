import {User} from "@/models/User";

class CookieService {
    getToken() {
        return localStorage.getItem("sharine-token") || ""
    }
    saveToken(token: string) {
        localStorage.setItem("sharine-token",token)
    }
    getAccount() {
        return localStorage.getItem("sharine-account") || ""
    }
    saveAccount(account: string) {
        localStorage.setItem("sharine-account",account)
    }
    savePassword(password: string) {
        localStorage.setItem("sharine-password",password)
    }
    getPassword() {
        return localStorage.getItem("sharine-password") || ""
    }
    saveLocalUser(user: User | null) {
        if(user !== null) {
            localStorage.setItem("sharine-local-user",JSON.stringify(user))
        } else localStorage.removeItem("sharine-local-user")
    }
    getLocalUser(): User | null {
        const str = localStorage.getItem("sharine-local-user")
        if(str === null) return null
        else return JSON.parse(str)
    }
}
export const cookieService = new CookieService()