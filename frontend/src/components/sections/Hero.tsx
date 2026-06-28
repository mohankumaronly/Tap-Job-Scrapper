import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { FiArrowRight, FiBriefcase, FiUsers, FiCheckCircle, FiAward } from 'react-icons/fi';
import EmailModal from '../auth/EmailModal';

const Hero: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fadeInUp = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 }
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

  const stats = [
    { icon: FiBriefcase, value: '12,847', label: 'Jobs Posted' },
    { icon: FiUsers, value: '3,241', label: 'Companies' },
    { icon: FiCheckCircle, value: '8,932', label: 'Active Users' },
  ];

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <>
      <section className="min-h-screen flex items-center pt-20 md:pt-24 section-padding">
        <div className="container-custom">
          <motion.div
            initial="hidden"
            animate="visible"
            variants={staggerContainer}
            className="text-center max-w-4xl mx-auto"
          >
            {/* Badge */}
            <motion.div
              variants={fadeInUp}
              className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-[rgb(var(--border))] border border-[rgb(var(--border))] mb-6"
            >
              <FiAward className="w-4 h-4 text-primary-600" />
              <span className="text-sm font-medium text-[rgb(var(--foreground))]/70">
                Built for Tap Academy Students
              </span>
            </motion.div>

            {/* Main Headline */}
            <motion.h1
              variants={fadeInUp}
              className="text-4xl md:text-6xl lg:text-7xl font-bold mb-6 leading-tight"
            >
              Never Miss a{' '}
              <span className="text-primary-600">Career Opportunity</span>
              {' '}Again
            </motion.h1>

            {/* Sub-headline */}
            <motion.p
              variants={fadeInUp}
              className="text-lg md:text-xl text-[rgb(var(--foreground))]/70 max-w-2xl mx-auto mb-8 leading-relaxed"
            >
              Get personalized job alerts delivered to your inbox. Join thousands 
              of Tap Academy students and professionals who found their dream jobs 
              through smart notifications.
            </motion.p>

            {/* CTA Container */}
            <motion.div
              variants={fadeInUp}
              className="flex flex-col sm:flex-row items-center justify-center gap-4 mb-8"
            >
              <button
                onClick={openModal}
                className="px-6 py-3 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors duration-200 shadow-sm hover:shadow-md flex items-center gap-2"
              >
                Get Started
                <FiArrowRight className="w-4 h-4" />
              </button>
              <a
                href="#features"
                className="px-6 py-3 text-sm font-medium text-[rgb(var(--foreground))] border border-[rgb(var(--border))] rounded-lg hover:bg-[rgb(var(--border))] transition-colors duration-200"
              >
                See Features
              </a>
            </motion.div>

            {/* Trust Badge */}
            <motion.p
              variants={fadeInUp}
              className="text-sm text-[rgb(var(--foreground))]/50 mb-8"
            >
              No credit card required • Free forever
            </motion.p>

            {/* Statistics Row */}
            <motion.div
              variants={fadeInUp}
              className="flex flex-wrap items-center justify-center gap-8 md:gap-12 pt-8 border-t border-[rgb(var(--border))]"
            >
              {stats.map((stat, index) => (
                <div key={index} className="flex items-center gap-3">
                  <stat.icon className="w-5 h-5 text-primary-600" />
                  <div>
                    <p className="text-2xl font-bold text-[rgb(var(--foreground))]">
                      {stat.value}
                    </p>
                    <p className="text-sm text-[rgb(var(--foreground))]/50">
                      {stat.label}
                    </p>
                  </div>
                </div>
              ))}
            </motion.div>
          </motion.div>
        </div>
      </section>

      {/* Email Modal */}
      <EmailModal isOpen={isModalOpen} onClose={closeModal} />
    </>
  );
};

export default Hero;