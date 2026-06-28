import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  FiBriefcase, 
  FiMapPin, 
  FiCalendar, 
  FiSearch, 
  FiFilter,
  FiChevronDown,
  FiX,
  FiExternalLink,
  FiClock,
  FiDollarSign,
  FiCheckCircle,
  FiXCircle
} from 'react-icons/fi';
import { jobService } from '../services/jobService';
import type { Job } from '../types/job';

const Jobs: React.FC = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [filteredJobs, setFilteredJobs] = useState<Job[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [showFilters, setShowFilters] = useState(false);
  const [selectedJob, setSelectedJob] = useState<Job | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [filters, setFilters] = useState({
    showActive: true,
    location: '',
    minPackage: '',
    showApplied: false
  });

  useEffect(() => {
    fetchJobs();
  }, []);

  useEffect(() => {
    filterJobs();
  }, [jobs, searchTerm, filters]);

  const fetchJobs = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await jobService.getActiveJobs();
      setJobs(response || []);
    } catch (err: any) {
      console.error('Error fetching jobs:', err);
      setError(err.response?.data?.message || 'Failed to load jobs. Please try again.');
      setJobs([]);
    } finally {
      setIsLoading(false);
    }
  };

  const filterJobs = () => {
    let filtered = [...jobs];

    // Filter by active status
    if (filters.showActive) {
      filtered = filtered.filter(job => !job.expired);
    }

    // Filter by applied status
    if (filters.showApplied) {
      filtered = filtered.filter(job => job.applied);
    }

    // Filter by search term
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      filtered = filtered.filter(job =>
        job.jobTitle.toLowerCase().includes(term) ||
        job.jobRole.toLowerCase().includes(term) ||
        job.location.toLowerCase().includes(term)
      );
    }

    // Filter by location
    if (filters.location) {
      filtered = filtered.filter(job =>
        job.location.toLowerCase().includes(filters.location.toLowerCase())
      );
    }

    // Filter by minimum package
    if (filters.minPackage) {
      const min = parseFloat(filters.minPackage);
      filtered = filtered.filter(job => job.packageLpa >= min);
    }

    setFilteredJobs(filtered);
  };

  const clearFilters = () => {
    setFilters({
      showActive: true,
      location: '',
      minPackage: '',
      showApplied: false
    });
    setSearchTerm('');
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-IN', {
      day: 'numeric',
      month: 'short',
      year: 'numeric'
    });
  };

  const formatTime = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString('en-IN', {
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getTimeAgo = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 0) return 'Today';
    if (diffDays === 1) return 'Yesterday';
    if (diffDays < 7) return `${diffDays} days ago`;
    if (diffDays < 30) return `${Math.floor(diffDays / 7)} weeks ago`;
    return `${Math.floor(diffDays / 30)} months ago`;
  };

  const isExpiringSoon = (expiresIn: string) => {
    const expiryDate = new Date(expiresIn);
    const now = new Date();
    const diffTime = expiryDate.getTime() - now.getTime();
    const diffDays = diffTime / (1000 * 60 * 60 * 24);
    return diffDays <= 2 && diffDays > 0;
  };

  // Loading skeleton
  if (isLoading) {
    return (
      <div className="min-h-screen pt-20 md:pt-24 section-padding">
        <div className="container-custom">
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[1, 2, 3, 4, 5, 6].map((i) => (
              <div key={i} className="animate-pulse">
                <div className="p-6 rounded-xl border border-[rgb(var(--border))]">
                  <div className="h-4 bg-[rgb(var(--border))] rounded w-3/4 mb-4"></div>
                  <div className="h-3 bg-[rgb(var(--border))] rounded w-1/2 mb-2"></div>
                  <div className="h-3 bg-[rgb(var(--border))] rounded w-1/3 mb-4"></div>
                  <div className="h-20 bg-[rgb(var(--border))] rounded mb-4"></div>
                  <div className="h-8 bg-[rgb(var(--border))] rounded w-1/3"></div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen pt-20 md:pt-24 section-padding">
      <div className="container-custom">
        {/* Header */}
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between mb-8 gap-4">
          <div>
            <h1 className="text-3xl md:text-4xl font-bold">
              <span className="text-primary-600">Job</span> Opportunities
            </h1>
            <p className="text-[rgb(var(--foreground))]/60 mt-1">
              {filteredJobs.length} jobs available for Tap Academy students
            </p>
          </div>
        </div>

        {/* Error Message */}
        {error && (
          <div className="mb-6 p-4 rounded-lg border border-red-500/20 bg-red-500/5 text-red-500 flex items-center justify-between">
            <span>{error}</span>
            <button onClick={() => setError(null)}>
              <FiX className="w-5 h-5" />
            </button>
          </div>
        )}

        {/* Search and Filters */}
        <div className="flex flex-col md:flex-row gap-4 mb-8">
          <div className="flex-1 relative">
            <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-[rgb(var(--foreground))]/40" />
            <input
              type="text"
              placeholder="Search jobs by title, role, or location..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))] text-[rgb(var(--foreground))] focus:outline-none focus:ring-2 focus:ring-primary-600"
            />
          </div>
          <button
            onClick={() => setShowFilters(!showFilters)}
            className="px-4 py-2.5 rounded-lg border border-[rgb(var(--border))] hover:bg-[rgb(var(--border))] transition-colors duration-200 flex items-center gap-2"
          >
            <FiFilter className="w-4 h-4" />
            Filters
            <FiChevronDown className={`w-4 h-4 transition-transform duration-200 ${showFilters ? 'rotate-180' : ''}`} />
          </button>
        </div>

        {/* Filters Panel */}
        <AnimatePresence>
          {showFilters && (
            <motion.div
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: 'auto' }}
              exit={{ opacity: 0, height: 0 }}
              className="overflow-hidden mb-8"
            >
              <div className="p-4 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))]">
                <div className="grid md:grid-cols-3 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-1.5">Status</label>
                    <div className="space-y-2">
                      <label className="flex items-center gap-2 text-sm">
                        <input
                          type="checkbox"
                          checked={filters.showActive}
                          onChange={(e) => setFilters({ ...filters, showActive: e.target.checked })}
                          className="rounded border-[rgb(var(--border))] text-primary-600 focus:ring-primary-600"
                        />
                        Show active jobs only
                      </label>
                      <label className="flex items-center gap-2 text-sm">
                        <input
                          type="checkbox"
                          checked={filters.showApplied}
                          onChange={(e) => setFilters({ ...filters, showApplied: e.target.checked })}
                          className="rounded border-[rgb(var(--border))] text-primary-600 focus:ring-primary-600"
                        />
                        Show applied jobs
                      </label>
                    </div>
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-1.5">Location</label>
                    <input
                      type="text"
                      placeholder="Filter by location"
                      value={filters.location}
                      onChange={(e) => setFilters({ ...filters, location: e.target.value })}
                      className="w-full px-3 py-1.5 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))] text-[rgb(var(--foreground))] focus:outline-none focus:ring-2 focus:ring-primary-600 text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-1.5">Min Package (LPA)</label>
                    <input
                      type="number"
                      placeholder="e.g. 3.5"
                      value={filters.minPackage}
                      onChange={(e) => setFilters({ ...filters, minPackage: e.target.value })}
                      className="w-full px-3 py-1.5 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))] text-[rgb(var(--foreground))] focus:outline-none focus:ring-2 focus:ring-primary-600 text-sm"
                    />
                  </div>
                </div>
                <button
                  onClick={clearFilters}
                  className="mt-4 text-sm text-primary-600 hover:underline"
                >
                  Clear all filters
                </button>
              </div>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Jobs Grid */}
        {filteredJobs.length === 0 ? (
          <div className="text-center py-12">
            <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-[rgb(var(--border))] flex items-center justify-center">
              <FiBriefcase className="w-10 h-10 text-[rgb(var(--foreground))]/40" />
            </div>
            <h3 className="text-xl font-semibold mb-2">No jobs found</h3>
            <p className="text-[rgb(var(--foreground))]/60">
              {searchTerm || filters.location || filters.minPackage 
                ? 'Try adjusting your filters or search terms'
                : 'Check back later for new opportunities'}
            </p>
          </div>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredJobs.map((job, index) => (
              <motion.div
                key={job.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.05 }}
                whileHover={{ y: -4 }}
                className="p-6 rounded-xl border border-[rgb(var(--border))] bg-[rgb(var(--background))] hover:shadow-lg transition-all duration-200 cursor-pointer"
                onClick={() => setSelectedJob(job)}
              >
                <div className="flex items-start justify-between mb-3">
                  <div className="flex-1">
                    <h3 className="font-semibold text-[rgb(var(--foreground))] line-clamp-2">
                      {job.jobTitle}
                    </h3>
                    <p className="text-sm text-primary-600 font-medium mt-0.5">
                      {job.jobRole}
                    </p>
                  </div>
                  <div className="flex flex-col items-end gap-1">
                    {!job.expired ? (
                      <span className="px-2 py-0.5 text-xs font-medium text-green-600 bg-green-600/10 rounded-full">
                        Active
                      </span>
                    ) : (
                      <span className="px-2 py-0.5 text-xs font-medium text-red-600 bg-red-600/10 rounded-full">
                        Expired
                      </span>
                    )}
                    {job.applied && (
                      <span className="px-2 py-0.5 text-xs font-medium text-blue-600 bg-blue-600/10 rounded-full">
                        Applied
                      </span>
                    )}
                    {!job.expired && isExpiringSoon(job.expiresIn) && (
                      <span className="px-2 py-0.5 text-xs font-medium text-yellow-600 bg-yellow-600/10 rounded-full">
                        Expiring soon
                      </span>
                    )}
                  </div>
                </div>

                <div className="space-y-2 mb-4">
                  {job.location && (
                    <div className="flex items-center gap-2 text-sm text-[rgb(var(--foreground))]/60">
                      <FiMapPin className="w-3.5 h-3.5" />
                      <span>{job.location}</span>
                    </div>
                  )}
                  {job.packageLpa && (
                    <div className="flex items-center gap-2 text-sm text-[rgb(var(--foreground))]/60">
                      <FiDollarSign className="w-3.5 h-3.5" />
                      <span>₹{job.packageLpa} LPA</span>
                    </div>
                  )}
                  {job.interviewDate && (
                    <div className="flex items-center gap-2 text-sm text-[rgb(var(--foreground))]/60">
                      <FiCalendar className="w-3.5 h-3.5" />
                      <span>Interview: {formatDate(job.interviewDate)} at {formatTime(job.interviewDate)}</span>
                    </div>
                  )}
                  {job.createdAt && (
                    <div className="flex items-center gap-2 text-sm text-[rgb(var(--foreground))]/60">
                      <FiClock className="w-3.5 h-3.5" />
                      <span>Posted {getTimeAgo(job.createdAt)}</span>
                    </div>
                  )}
                </div>

                <div className="flex items-center justify-between pt-4 border-t border-[rgb(var(--border))]">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      setSelectedJob(job);
                    }}
                    className="text-sm font-medium text-primary-600 hover:text-primary-700 transition-colors duration-200 flex items-center gap-1"
                  >
                    View Details
                    <FiExternalLink className="w-3.5 h-3.5" />
                  </button>
                  {!job.applied && !job.expired && (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        // Handle apply action
                      }}
                      className="text-sm font-medium bg-primary-600 text-white px-3 py-1 rounded-lg hover:bg-primary-700 transition-colors duration-200"
                    >
                      Apply Now
                    </button>
                  )}
                </div>
              </motion.div>
            ))}
          </div>
        )}
      </div>

      {/* Job Detail Modal */}
      <AnimatePresence>
        {selectedJob && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4"
            onClick={() => setSelectedJob(null)}
          >
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              transition={{ type: 'spring', damping: 25 }}
              className="bg-[rgb(var(--background))] rounded-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto p-6 md:p-8 border border-[rgb(var(--border))]"
              onClick={(e) => e.stopPropagation()}
            >
              <div className="flex items-start justify-between mb-6">
                <div>
                  <h2 className="text-2xl font-bold text-[rgb(var(--foreground))]">
                    {selectedJob.jobTitle}
                  </h2>
                  <p className="text-primary-600 font-medium mt-1">
                    {selectedJob.jobRole}
                  </p>
                </div>
                <button
                  onClick={() => setSelectedJob(null)}
                  className="p-2 rounded-lg hover:bg-[rgb(var(--border))] transition-colors duration-200"
                >
                  <FiX className="w-5 h-5 text-[rgb(var(--foreground))]" />
                </button>
              </div>

              <div className="space-y-4">
                <div className="flex flex-wrap gap-4 text-sm text-[rgb(var(--foreground))]/60">
                  {selectedJob.location && (
                    <div className="flex items-center gap-2">
                      <FiMapPin className="w-4 h-4" />
                      <span>{selectedJob.location}</span>
                    </div>
                  )}
                  {selectedJob.packageLpa && (
                    <div className="flex items-center gap-2">
                      <FiDollarSign className="w-4 h-4" />
                      <span>₹{selectedJob.packageLpa} LPA</span>
                    </div>
                  )}
                  {selectedJob.interviewDate && (
                    <div className="flex items-center gap-2">
                      <FiCalendar className="w-4 h-4" />
                      <span>Interview: {formatDate(selectedJob.interviewDate)} at {formatTime(selectedJob.interviewDate)}</span>
                    </div>
                  )}
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="p-4 rounded-lg border border-[rgb(var(--border))]">
                    <p className="text-sm font-medium">Status</p>
                    <div className="flex items-center gap-2 mt-1">
                      {!selectedJob.expired ? (
                        <>
                          <FiCheckCircle className="w-4 h-4 text-green-600" />
                          <span className="text-sm text-green-600">Active</span>
                        </>
                      ) : (
                        <>
                          <FiXCircle className="w-4 h-4 text-red-600" />
                          <span className="text-sm text-red-600">Expired</span>
                        </>
                      )}
                    </div>
                  </div>
                  <div className="p-4 rounded-lg border border-[rgb(var(--border))]">
                    <p className="text-sm font-medium">Application</p>
                    <div className="flex items-center gap-2 mt-1">
                      {selectedJob.applied ? (
                        <>
                          <FiCheckCircle className="w-4 h-4 text-blue-600" />
                          <span className="text-sm text-blue-600">Applied</span>
                        </>
                      ) : (
                        <>
                          <FiXCircle className="w-4 h-4 text-gray-600" />
                          <span className="text-sm text-gray-600">Not Applied</span>
                        </>
                      )}
                    </div>
                  </div>
                </div>

                <div className="p-4 rounded-lg border border-[rgb(var(--border))]">
                  <h4 className="font-semibold mb-2">Important Dates</h4>
                  <div className="space-y-2 text-sm">
                    <div className="flex justify-between">
                      <span className="text-[rgb(var(--foreground))]/60">Posted:</span>
                      <span>{formatDate(selectedJob.createdAt)}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-[rgb(var(--foreground))]/60">Expires:</span>
                      <span className={isExpiringSoon(selectedJob.expiresIn) && !selectedJob.expired ? 'text-yellow-600 font-medium' : ''}>
                        {formatDate(selectedJob.expiresIn)}
                      </span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-[rgb(var(--foreground))]/60">Interview:</span>
                      <span>{formatDate(selectedJob.interviewDate)} at {formatTime(selectedJob.interviewDate)}</span>
                    </div>
                  </div>
                </div>

                {!selectedJob.applied && !selectedJob.expired && (
                  <button className="w-full py-3 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors duration-200">
                    Apply Now
                  </button>
                )}
              </div>
            </motion.div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default Jobs;