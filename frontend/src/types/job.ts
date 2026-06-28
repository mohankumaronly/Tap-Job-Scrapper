export interface Job {
  id: number;
  jobId: number;
  jobTitle: string;
  jobRole: string;
  packageLpa: number;
  location: string;
  expired: boolean;
  applied: boolean;
  interviewDate: string;
  expiresIn: string;
  createdAt: string;
}

export interface JobsResponse {
  jobs: Job[];
  total?: number;
  message?: string;
}