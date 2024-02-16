import {AxiosError} from "axios";
import {ErrorResponse} from "@/services/ErrorResponse";
import {Api} from "@/services/ApiService";

export interface UserPublicData {
  login: string,
  email: string,
  phone: string
}

export interface UserTokenData {
  userData: UserPublicData,
  jsonAuth: string
}

export interface UsernameForm {
  username: string
}

export interface LoginForm {
    username: string,
    password: string
}

export interface RegisterForm {
  login: string,
  password: string,
  email: string,
  phone: string
}

export interface UpdateUserForm {
  email: string | null,
  phone: string | null
}

export class UserService {
  public async getUserByUserName(userNameQuery: string): Promise<ErrorResponse | UserPublicData> {
    try {
      const api = new Api();
      return await api.get<UserPublicData>('/api/user/' + userNameQuery);
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async authorizeUser(form: LoginForm): Promise<ErrorResponse | UserTokenData> {
    try {
      const api = new Api();
      const res = await api.post<UserTokenData, LoginForm>('/api/user/login', form);
      return res;
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async registerUser(form: RegisterForm): Promise<ErrorResponse | UserTokenData> {
    try {
      const api = new Api();
      return await api.post<UserTokenData, RegisterForm>('/api/user', form);
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async updateUserInfo(form: UpdateUserForm): Promise<ErrorResponse | UserPublicData> {
    try {
      const api = new Api();
      return await api.put<UserPublicData, UpdateUserForm>('/api/user', form);
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }

  public async findUsersByUsername(username: string): Promise<ErrorResponse | UserPublicData[]> {
    try {
      const api = new Api();
      return await api.get<UserPublicData[]>('/api/user?userName=' + username);
    } catch (e) {
      const error = e as AxiosError;
      if (error.response != null)
        return error.response.data as ErrorResponse;
      throw e;
    }
  }
}