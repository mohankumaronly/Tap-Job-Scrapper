export interface Job {
  id: number;
  title: string;
  company: string;
  location: string;
  description: string;
  requirements?: string[];
  salary?: string;
  postedAt: string;
  isActive: boolean;
  portalJobId?: string;
  url?: string;
}

export interface JobsResponse {
  jobs: Job[];
  total?: number;
  message?: string;
}