import {ErrorResponse} from "@/services/ErrorResponse";
import {Api} from "@/services/ApiService";
import {AxiosError} from "axios";
import {GroupData} from "@/services/GroupService";
import {UsernameForm, UserPublicData} from "@/services/UserService";

export interface ScriptInput {
    name: string,
    type: string,
    value: string
}

export interface ScriptData {
    id: number,
    code: string,
    name: string,
    description: string,
    inputData: ScriptInput[],
    version: string,
    os: "Windows" | "Darwin"
}

export interface ScriptForm {
    code: string,
    name: string,
    description: string,
    inputData: ScriptInput[],
    version: string,
    os: "Windows" | "Darwin"
}

export interface ManagingScript {
    scriptResponse: ScriptData,
    creator: boolean
}

export interface ScriptAuthorsExecutors {
    executors: GroupData[],
    authors: UserPublicData
}

export class ScriptService {
    public async revokeRightsFromUser(scriptId: number, username: string): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            await api.delete('/api/script/' + scriptId + '/author/' + username);
            return null;
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async getExecutingScripts(): Promise<ErrorResponse | ScriptData[]> {
        try {
            const api = new Api();
            return await api.get<ScriptData[]>('/api/script/execute');
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async getManagingScripts(): Promise<ErrorResponse | ManagingScript[]> {
        try {
            const api = new Api();
            return await api.get<ManagingScript[]>('/api/script/manage');
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async getScriptAuthorsExecutors(scriptId: number): Promise<ErrorResponse | ScriptAuthorsExecutors> {
        try {
            const api = new Api();
            return await api.get<ScriptAuthorsExecutors>('/api/script/' + scriptId);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async allowUserToManageScript(scriptId: number, username: UsernameForm): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            return await api.post<null, UsernameForm>('/api/script/' + scriptId + '/author', username);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async revokeRightsToExecuteScript(scriptId: number, groupId: number): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            await api.delete('/api/script/' + scriptId + '/group/' + groupId);
            return null;
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async allowGroupToExecuteScript(scriptId: number, groupId: number): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            return await api.post<null, null>('/api/script/' + scriptId + '/group/' + groupId, null);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async createScript(script: ScriptForm): Promise<ErrorResponse | ScriptData> {
        try {
            const api = new Api();
            return await api.post<ScriptData, ScriptForm>('/api/script', script);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async deleteScript(scriptId: number): Promise<ErrorResponse | null> {
        try {
            const api = new Api();
            await api.delete('/api/script/' + scriptId);
            return null;
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async updateScript(script: ScriptForm, scriptId: number): Promise<ErrorResponse | ScriptData> {
        try {
            const api = new Api();
            return await api.put<ScriptData, ScriptForm>('/api/script/' + scriptId, script);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }
}