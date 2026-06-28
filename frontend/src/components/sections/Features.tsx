import React from 'react';
import { motion } from 'framer-motion';
import { 
  FiBell, 
  FiStar,  // Changed from FiSparkles to FiStar
  FiZap, 
  FiMail, 
  FiXCircle, 
  FiLink,
  FiClock,
  FiTrendingUp
} from 'react-icons/fi';

const Features: React.FC = () => {
  const fadeInUp = {
    hidden: { opacity: 0, y: 30 },
    visible: { opacity: 1, y: 0 }
  };

  const staggerContainer = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const features = [
    {
      icon: FiBell,
      title: 'Real-Time Job Tracking',
      description: 'Instant notifications for new positions matching your skills and preferences.',
      color: 'text-blue-600'
    },
    {
      icon: FiStar,  // Changed from FiSparkles to FiStar
      title: 'Smart Matching Algorithm',
      description: 'AI-powered job recommendations tailored to your experience and interests.',
      color: 'text-purple-600'
    },
    {
      icon: FiZap,
      title: 'One-Click Apply',
      description: 'Apply to jobs instantly with your saved profile and resume.',
      color: 'text-yellow-600'
    },
    {
      icon: FiMail,
      title: 'Email Management',
      description: 'Control the frequency and preferences of your job alerts.',
      color: 'text-green-600'
    },
    {
      icon: FiXCircle,
      title: 'Easy Unsubscribe',
      description: 'Opt-out anytime with just one click. No questions asked.',
      color: 'text-red-600'
    },
    {
      icon: FiLink,
      title: 'Multi-Platform Sync',
      description: 'Access your job alerts anywhere, anytime across all devices.',
      color: 'text-indigo-600'
    }
  ];

  return (
    <section id="features" className="section-padding bg-[rgb(var(--background))]">
      <div className="container-custom">
        {/* Section Header */}
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.3 }}
          variants={staggerContainer}
          className="text-center max-w-3xl mx-auto mb-16"
        >
          <motion.p
            variants={fadeInUp}
            className="text-sm font-medium text-primary-600 mb-3"
          >
            FEATURES
          </motion.p>
          <motion.h2
            variants={fadeInUp}
            className="text-3xl md:text-4xl font-bold mb-4"
          >
            Everything You Need to{' '}
            <span className="text-primary-600">Land Your Dream Job</span>
          </motion.h2>
          <motion.p
            variants={fadeInUp}
            className="text-[rgb(var(--foreground))]/70 text-lg"
          >
            Smart tools designed to keep you ahead in your career journey
          </motion.p>
        </motion.div>

        {/* Features Grid */}
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.1 }}
          variants={staggerContainer}
          className="grid md:grid-cols-2 lg:grid-cols-3 gap-6"
        >
          {features.map((feature, index) => (
            <motion.div
              key={index}
              variants={fadeInUp}
              whileHover={{ 
                y: -4,
                transition: { duration: 0.2 }
              }}
              className="p-6 rounded-xl border border-[rgb(var(--border))] bg-[rgb(var(--background))] hover:shadow-lg transition-shadow duration-300"
            >
              <div className="w-12 h-12 rounded-lg bg-primary-600/10 flex items-center justify-center mb-4">
                <feature.icon className={`w-6 h-6 ${feature.color}`} />
              </div>
              <h3 className="text-lg font-semibold mb-2">
                {feature.title}
              </h3>
              <p className="text-sm text-[rgb(var(--foreground))]/70 leading-relaxed">
                {feature.description}
              </p>
            </motion.div>
          ))}
        </motion.div>

        {/* Additional Stats */}
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.3 }}
          variants={staggerContainer}
          className="mt-16 grid md:grid-cols-3 gap-6 pt-8 border-t border-[rgb(var(--border))]"
        >
          {[
            { icon: FiClock, value: '24/7', label: 'Job Updates' },
            { icon: FiTrendingUp, value: '95%', label: 'Success Rate' },
            { icon: FiBell, value: '10K+', label: 'Active Jobs' }
          ].map((stat, index) => (
            <motion.div
              key={index}
              variants={fadeInUp}
              className="text-center"
            >
              <stat.icon className="w-6 h-6 text-primary-600 mx-auto mb-2" />
              <p className="text-2xl font-bold text-[rgb(var(--foreground))]">
                {stat.value}
              </p>
              <p className="text-sm text-[rgb(var(--foreground))]/50">
                {stat.label}
              </p>
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  );
};

export default Features;