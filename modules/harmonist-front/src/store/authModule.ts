import {Commit, Module} from "vuex";
import {AuthState, StoreState} from "@/store/types";
import {UserPublicData} from "@/services/UserService";

export const authModule: Module<AuthState, StoreState> = {
    state: (): AuthState => ({
        isAuth: false,
        token: '',
        data: null
    }),
    mutations: {
        SET_IS_AUTH(state: AuthState, isAuth: boolean) {
            state.isAuth = isAuth;
        },
        SET_TOKEN(state: AuthState, token: string) {
            state.token = token;
        },
        SET_DATA(state: AuthState, data: UserPublicData) {
            state.data = data
        }
    },
    actions: {
        loggedIn({commit}: {commit: Commit}, {token, data}: {token: string, data: UserPublicData}) {
            commit('SET_IS_AUTH', true);
            commit('SET_TOKEN', token);
            commit('SET_DATA', data);
        },
        loggedOut({commit}: {commit: Commit}) {
            commit('SET_IS_AUTH', false);
            commit('SET_TOKEN', '');
            commit('SET_DATA', null)
        },
        saveData({commit}: {commit: Commit}, {data}: {data: UserPublicData}) {
            commit('SET_DATA', data)
        }
    },
    getters: {
        isAuth: (state: AuthState) => state.isAuth,
        token: (state: AuthState) => state.token,
        username: (state: AuthState) => state.data?.login,
        data: (state: AuthState) => state.data
    },
    namespaced: true
}