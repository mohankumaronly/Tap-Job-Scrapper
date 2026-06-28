import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  FiBriefcase, 
  FiMapPin, 
  FiCalendar, 
  FiSearch, 
  FiRefreshCw,
  FiFilter,
  FiChevronDown,
  FiX,
  FiExternalLink,
  FiClock
} from 'react-icons/fi';
import { jobService } from '../services/jobService';
import type { Job } from '../types/job';

const Jobs: React.FC = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [filteredJobs, setFilteredJobs] = useState<Job[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isSyncing, setIsSyncing] = useState(false);
  const [showFilters, setShowFilters] = useState(false);
  const [selectedJob, setSelectedJob] = useState<Job | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [filters, setFilters] = useState({
    showActive: true,
    location: '',
    company: ''
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
      setJobs(response.jobs || []);
    } catch (err: any) {
      console.error('Error fetching jobs:', err);
      setError(err.response?.data?.message || 'Failed to load jobs. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSync = async () => {
    setIsSyncing(true);
    setError(null);
    try {
      await jobService.syncJobs();
      await fetchJobs();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to sync jobs.');
    } finally {
      setIsSyncing(false);
    }
  };

  const filterJobs = () => {
    let filtered = [...jobs];

    // Filter by active status
    if (filters.showActive) {
      filtered = filtered.filter(job => job.isActive);
    }

    // Filter by search term
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      filtered = filtered.filter(job =>
        job.title.toLowerCase().includes(term) ||
        job.company.toLowerCase().includes(term) ||
        job.location.toLowerCase().includes(term) ||
        job.description.toLowerCase().includes(term)
      );
    }

    // Filter by location
    if (filters.location) {
      filtered = filtered.filter(job =>
        job.location.toLowerCase().includes(filters.location.toLowerCase())
      );
    }

    // Filter by company
    if (filters.company) {
      filtered = filtered.filter(job =>
        job.company.toLowerCase().includes(filters.company.toLowerCase())
      );
    }

    setFilteredJobs(filtered);
  };

  const clearFilters = () => {
    setFilters({
      showActive: true,
      location: '',
      company: ''
    });
    setSearchTerm('');
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
          <button
            onClick={handleSync}
            disabled={isSyncing}
            className="px-4 py-2 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors duration-200 flex items-center gap-2 disabled:opacity-50"
          >
            <FiRefreshCw className={`w-4 h-4 ${isSyncing ? 'animate-spin' : ''}`} />
            {isSyncing ? 'Syncing...' : 'Sync Jobs'}
          </button>
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
              placeholder="Search jobs by title, company, or location..."
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
                    <label className="flex items-center gap-2 text-sm">
                      <input
                        type="checkbox"
                        checked={filters.showActive}
                        onChange={(e) => setFilters({ ...filters, showActive: e.target.checked })}
                        className="rounded border-[rgb(var(--border))] text-primary-600 focus:ring-primary-600"
                      />
                      Show active jobs only
                    </label>
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
                    <label className="block text-sm font-medium mb-1.5">Company</label>
                    <input
                      type="text"
                      placeholder="Filter by company"
                      value={filters.company}
                      onChange={(e) => setFilters({ ...filters, company: e.target.value })}
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
              {searchTerm || filters.location || filters.company 
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
                      {job.title}
                    </h3>
                    <p className="text-sm text-primary-600 font-medium mt-0.5">
                      {job.company}
                    </p>
                  </div>
                  {job.isActive && (
                    <span className="px-2 py-0.5 text-xs font-medium text-green-600 bg-green-600/10 rounded-full">
                      Active
                    </span>
                  )}
                </div>

                <div className="space-y-2 mb-4">
                  {job.location && (
                    <div className="flex items-center gap-2 text-sm text-[rgb(var(--foreground))]/60">
                      <FiMapPin className="w-3.5 h-3.5" />
                      <span>{job.location}</span>
                    </div>
                  )}
                  {job.postedAt && (
                    <div className="flex items-center gap-2 text-sm text-[rgb(var(--foreground))]/60">
                      <FiClock className="w-3.5 h-3.5" />
                      <span>Posted {getTimeAgo(job.postedAt)}</span>
                    </div>
                  )}
                </div>

                <p className="text-sm text-[rgb(var(--foreground))]/70 line-clamp-3 mb-4">
                  {job.description}
                </p>

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
                    {selectedJob.title}
                  </h2>
                  <p className="text-primary-600 font-medium mt-1">
                    {selectedJob.company}
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
                  {selectedJob.postedAt && (
                    <div className="flex items-center gap-2">
                      <FiCalendar className="w-4 h-4" />
                      <span>Posted {getTimeAgo(selectedJob.postedAt)}</span>
                    </div>
                  )}
                  {selectedJob.salary && (
                    <div className="flex items-center gap-2">
                      <FiBriefcase className="w-4 h-4" />
                      <span>{selectedJob.salary}</span>
                    </div>
                  )}
                </div>

                <div className="p-4 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))]">
                  <h4 className="font-semibold mb-2">Description</h4>
                  <p className="text-sm text-[rgb(var(--foreground))]/70 whitespace-pre-wrap">
                    {selectedJob.description}
                  </p>
                </div>

                {selectedJob.requirements && selectedJob.requirements.length > 0 && (
                  <div className="p-4 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))]">
                    <h4 className="font-semibold mb-2">Requirements</h4>
                    <ul className="list-disc list-inside space-y-1">
                      {selectedJob.requirements.map((req, idx) => (
                        <li key={idx} className="text-sm text-[rgb(var(--foreground))]/70">
                          {req}
                        </li>
                      ))}
                    </ul>
                  </div>
                )}

                {selectedJob.url && (
                  <a
                    href={selectedJob.url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="inline-flex items-center gap-2 text-primary-600 hover:text-primary-700 font-medium"
                  >
                    Apply Now <FiExternalLink className="w-4 h-4" />
                  </a>
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