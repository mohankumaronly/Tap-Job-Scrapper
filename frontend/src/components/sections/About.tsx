import React from 'react';
import { motion } from 'framer-motion';
import { 
  FiShield, 
  FiBell, 
  FiZap, 
  FiHeart,
  FiUsers,
  FiBriefcase,
  FiGlobe,
  FiAlertCircle,
  FiClock
} from 'react-icons/fi';

const About: React.FC = () => {
  const fadeInUp = {
    hidden: { opacity: 0, y: 30 },
    visible: { opacity: 1, y: 0 }
  };

  const fadeInRight = {
    hidden: { opacity: 0, x: 30 },
    visible: { opacity: 1, x: 0 }
  };

  const staggerContainer = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.15
      }
    }
  };

  const values = [
    {
      icon: FiBell,
      title: 'Instant Job Alerts',
      description: 'Get notified immediately when new internships open'
    },
    {
      icon: FiClock,
      title: 'Never Miss Out',
      description: 'Stay ahead of other applicants with real-time updates'
    },
    {
      icon: FiShield,
      title: 'Verified Opportunities',
      description: 'Only genuine internships from trusted companies'
    },
    {
      icon: FiHeart,
      title: 'Student First',
      description: 'Built specifically for Tap Academy interns like you'
    }
  ];

  const stats = [
    { icon: FiUsers, value: '5,000+', label: 'Tap Students' },
    { icon: FiBriefcase, value: '2,000+', label: 'Internships Posted' },
    { icon: FiGlobe, value: '500+', label: 'Partner Companies' }
  ];

  return (
    <section id="about" className="section-padding bg-[rgb(var(--background))]">
      <div className="container-custom">
        <div className="grid lg:grid-cols-2 gap-8 md:gap-12 items-start">
          {/* Left Column - Text Content */}
          <motion.div
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, amount: 0.3 }}
            variants={staggerContainer}
            className="order-2 lg:order-1"
          >
            <motion.p
              variants={fadeInUp}
              className="text-sm font-medium text-primary-600 mb-3"
            >
              ABOUT US
            </motion.p>
            <motion.h2
              variants={fadeInUp}
              className="text-2xl sm:text-3xl md:text-4xl font-bold mb-4"
            >
              Never Miss an{' '}
              <span className="text-primary-600">Internship Opportunity</span>{' '}
              Again
            </motion.h2>
            <motion.p
              variants={fadeInUp}
              className="text-[rgb(var(--foreground))]/70 text-base md:text-lg mb-6 leading-relaxed"
            >
              We noticed a problem — official Tap Academy notifications don't 
              always reach interns when new job openings happen. So we built a 
              solution.
            </motion.p>
            <motion.p
              variants={fadeInUp}
              className="text-[rgb(var(--foreground))]/70 text-base md:text-lg mb-6 leading-relaxed"
            >
              JobAlert is designed specifically for Tap Academy interns who want 
              to stay ahead. We monitor job openings and send you instant alerts 
              so you never miss an opportunity to apply.
            </motion.p>

            {/* Problem Box */}
            <motion.div
              variants={fadeInUp}
              className="p-4 mb-6 rounded-lg border border-primary-600/20 bg-primary-600/5"
            >
              <div className="flex items-start gap-3">
                <FiAlertCircle className="w-5 h-5 text-primary-600 mt-0.5 flex-shrink-0" />
                <div>
                  <h4 className="text-sm font-semibold">The Problem We Solve</h4>
                  <p className="text-sm text-[rgb(var(--foreground))]/70">
                    Official Tap notifications don't always reach interns. We make 
                    sure you're the first to know.
                  </p>
                </div>
              </div>
            </motion.div>

            {/* Values Grid - Mobile friendly */}
            <motion.div
              variants={fadeInUp}
              className="grid grid-cols-1 sm:grid-cols-2 gap-3 md:gap-4"
            >
              {values.map((value, index) => (
                <div key={index} className="flex items-start gap-3 p-3 rounded-lg border border-[rgb(var(--border))]">
                  <value.icon className="w-5 h-5 text-primary-600 mt-0.5 flex-shrink-0" />
                  <div>
                    <h4 className="text-sm font-semibold">{value.title}</h4>
                    <p className="text-xs text-[rgb(var(--foreground))]/60">
                      {value.description}
                    </p>
                  </div>
                </div>
              ))}
            </motion.div>
          </motion.div>

          {/* Right Column - Visual/Stats */}
          <motion.div
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, amount: 0.3 }}
            variants={staggerContainer}
            className="order-1 lg:order-2 space-y-6"
          >
            {/* Main Card */}
            <motion.div
              variants={fadeInRight}
              className="p-6 md:p-8 rounded-2xl border border-[rgb(var(--border))] bg-[rgb(var(--background))]"
            >
              <div className="text-center mb-6">
                <div className="w-14 h-14 md:w-16 md:h-16 mx-auto mb-4 rounded-full bg-primary-600/10 flex items-center justify-center">
                  <FiBell className="w-7 h-7 md:w-8 md:h-8 text-primary-600" />
                </div>
                <h3 className="text-lg md:text-xl font-bold">Built for Tap Academy Interns</h3>
                <p className="text-xs md:text-sm text-[rgb(var(--foreground))]/70 mt-2">
                  We fill the gap when official notifications fall through
                </p>
              </div>

              {/* Stats */}
              <div className="space-y-3 md:space-y-4">
                {stats.map((stat, index) => (
                  <motion.div
                    key={index}
                    variants={fadeInUp}
                    className="flex items-center justify-between p-3 rounded-lg border border-[rgb(var(--border))]"
                  >
                    <div className="flex items-center gap-2 md:gap-3">
                      <stat.icon className="w-4 h-4 md:w-5 md:h-5 text-primary-600" />
                      <span className="text-xs md:text-sm font-medium">{stat.label}</span>
                    </div>
                    <span className="text-base md:text-lg font-bold text-primary-600">
                      {stat.value}
                    </span>
                  </motion.div>
                ))}
              </div>
            </motion.div>

            {/* Trust Badge */}
            <motion.div
              variants={fadeInRight}
              className="text-center p-3 md:p-4 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))]"
            >
              <p className="text-xs md:text-sm text-[rgb(var(--foreground))]/60">
                🎯 Your personal internship alert system
              </p>
            </motion.div>
          </motion.div>
        </div>
      </div>
    </section>
  );
};

export default About;