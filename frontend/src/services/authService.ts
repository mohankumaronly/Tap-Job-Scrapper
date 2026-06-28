import api from './api';

export interface SendOTPRequest {
  email: string;
}

export interface VerifyOTPRequest {
  email: string;
  otp: string;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  token?: string;
}

export const authService = {
  // Send OTP to email
  sendOTP: async (email: string): Promise<AuthResponse> => {
    try {
      const response = await api.post<AuthResponse>('/api/auth/send-otp', { email });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Verify OTP
  verifyOTP: async (email: string, otp: string): Promise<AuthResponse> => {
    try {
      const response = await api.post<AuthResponse>('/api/auth/verify-otp', { email, otp });
      if (response.data.token) {
        localStorage.setItem('authToken', response.data.token);
      }
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Unsubscribe
  unsubscribe: async (email: string): Promise<AuthResponse> => {
    try {
      const response = await api.post<AuthResponse>(`/api/auth/unsubscribe?email=${email}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Logout
  logout: () => {
    localStorage.removeItem('authToken');
  },

  // Check if authenticated
  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('authToken');
  }
};