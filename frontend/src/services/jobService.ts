import api from './api';
import type { Job, JobsResponse } from '../types/job';

export const jobService = {
  // Get all jobs
  getAllJobs: async (): Promise<Job[]> => {
    try {
      const response = await api.get<Job[]>('/api/jobs');
      return response.data;
    } catch (error) {
      console.error('Error fetching jobs:', error);
      throw error;
    }
  },

  // Get active jobs (not expired)
  getActiveJobs: async (): Promise<Job[]> => {
    try {
      const response = await api.get<Job[]>('/api/jobs/active');
      return response.data;
    } catch (error) {
      console.error('Error fetching active jobs:', error);
      throw error;
    }
  },

  // Get job by ID
  getJobById: async (jobId: number): Promise<Job> => {
    try {
      const response = await api.get<Job>(`/api/jobs/${jobId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching job:', error);
      throw error;
    }
  },

  // Sync jobs (manual sync)
  syncJobs: async (): Promise<{ message: string }> => {
    try {
      const response = await api.post<{ message: string }>('/api/jobs/sync');
      return response.data;
    } catch (error) {
      console.error('Error syncing jobs:', error);
      throw error;
    }
  },

  // Test job alert email
  testJobAlert: async (email: string): Promise<{ message: string }> => {
    try {
      const response = await api.post<{ message: string }>(
        `/api/email/test/job-alert?email=${encodeURIComponent(email)}`
      );
      return response.data;
    } catch (error) {
      console.error('Error sending test email:', error);
      throw error;
    }
  }
}; 