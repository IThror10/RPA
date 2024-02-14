import {AxiosError} from "axios";
import {ErrorResponse} from "@/services/ErrorResponse";
import {Api} from "@/services/ApiService";
import {ScriptInput} from "@/services/ScriptService";

export interface RobotOutput {
    name: string,
    type: string,
    value: object
}

export interface RobotData {
    os: "Windows" | "Darwin",
    version: string
}

export interface RobotError {
    message: string
}

export interface VersionData {
    id: number,
    version: string,
    compatibleFrom: string
}

export interface RobotScript {
    code: string,
    inputData: ScriptInput[],
    version: string,
    os: "Windows" | "Darwin"
}

export interface CompileOutput {
    message: string
}

export interface ExecuteOutput {
    message: string,
    output: RobotOutput[]
}

export class RobotService {
    public async executeScript(script: RobotScript): Promise<ErrorResponse | ExecuteOutput | RobotError> {
        try {
            const api = new Api();
            return await api.post<ExecuteOutput | RobotError, RobotScript>('/api/robot/execute', script);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async compileScript(script: RobotScript): Promise<ErrorResponse | CompileOutput | RobotError> {
        try {
            const api = new Api();
            return await api.post<CompileOutput | RobotError, RobotScript>('/api/robot/compile', script);
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async availableRobots(): Promise<ErrorResponse | RobotData[]> {
        try {
            const api = new Api();
            return await api.get<RobotData[]>('/api/robot/available');
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async getManual(versionId: number): Promise<ErrorResponse | string> {
        try {
            const api = new Api();
            return await api.get<string>('/api/robot/version/' + versionId + "/man");
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }

    public async existingVersions(): Promise<ErrorResponse | VersionData[]> {
        try {
            const api = new Api();
            return await api.get<VersionData[]>('/api/robot/version');
        } catch (e) {
            const error = e as AxiosError;
            if (error.response != null)
                return error.response.data as ErrorResponse;
            throw e;
        }
    }
}