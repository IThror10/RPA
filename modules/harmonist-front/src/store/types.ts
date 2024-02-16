import {UserPublicData} from "@/services/UserService";
export interface AuthState {
    isAuth: boolean,
    token: string,
    data: UserPublicData | null,
}

export interface StoreState {
    auth: AuthState
}