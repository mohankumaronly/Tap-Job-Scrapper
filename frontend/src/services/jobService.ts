import api from './api';
import type { Job, JobsResponse } from '../types/job';

export const jobService = {
  // Get all jobs
  getAllJobs: async (): Promise<JobsResponse> => {
    try {
      const response = await api.get<JobsResponse>('/api/jobs');
      return response.data;
    } catch (error) {
      console.error('Error fetching jobs:', error);
      throw error;
    }
  },

  // Get active jobs
  getActiveJobs: async (): Promise<JobsResponse> => {
    try {
      const response = await api.get<JobsResponse>('/api/jobs/active');
      return response.data;
    } catch (error) {
      console.error('Error fetching active jobs:', error);
      throw error;
    }
  },

  // Get job by ID
  getJobById: async (jobId: string): Promise<{ job: Job }> => {
    try {
      const response = await api.get<{ job: Job }>(`/api/jobs/${jobId}`);
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