import {AxiosError} from "axios";
import {ErrorResponse} from "@/services/ErrorResponse";
import {Api} from "@/services/ApiService";
import {UsernameForm, UserPublicData} from "@/services/UserService";

export interface GroupData {
    id: number,
    name: string,
    description: string
}

export interface UsersGroup {
    groupResponse: GroupData,
    groupRole: "LEADER" | "MEMBER" | "INVITED"
}

export interface GroupsMember {
    userInfoResponse: UserPublicData,
    groupRole: "LEADER" | "MEMBER" | "INVITED"
}

export interface GroupForm {
    name: string,
    description: string
}

export class GroupService {
    public async groupUserAction(action: "accept" | "leave" | "decline", groupId: number): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            return await api.post<null, null>('/api/group/' + groupId + "/" + action);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async groupLeaderAction(action: "remove" | "invite", groupId: number, username: UsernameForm): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            return await api.post<null, UsernameForm>('/api/group/' + groupId + "/" + action, username);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async createGroup(group: GroupForm): Promise<ErrorResponse | GroupData> {
        try {
            const api = new Api();
            return await api.post<GroupData, GroupForm>('/api/group', group);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async getUsersGroups(): Promise<ErrorResponse | UsersGroup[]> {
        try {
            const api = new Api();
            return await api.get<UsersGroup[]>('/api/group');
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async deleteGroup(groupId: number): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            await api.delete('/api/group/' + groupId);
            return null;
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async updateGroup(group: GroupForm, groupId: number): Promise<ErrorResponse | GroupData> {
        try {
            const api = new Api();
            return await api.post<GroupData, GroupForm>('/api/group/' + groupId, group);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async getGroupsMember(groupId: number): Promise<ErrorResponse | GroupsMember[]> {
        try {
            const api = new Api();
            return await api.get<GroupsMember[]>('/api/group/' + groupId);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }
}