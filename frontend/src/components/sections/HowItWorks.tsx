import React from 'react';
import { motion } from 'framer-motion';
import { FiMail, FiShield, FiBell, FiArrowRight } from 'react-icons/fi';

const HowItWorks: React.FC = () => {
  const fadeInUp = {
    hidden: { opacity: 0, y: 30 },
    visible: { opacity: 1, y: 0 }
  };

  const staggerContainer = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.2
      }
    }
  };

  const steps = [
    {
      icon: FiMail,
      number: '01',
      title: 'Enter Your Email',
      description: 'Tell us what roles and technologies you\'re interested in. We\'ll customize alerts just for you.',
      color: 'text-blue-600'
    },
    {
      icon: FiShield,
      number: '02',
      title: 'Verify Your Identity',
      description: 'Secure OTP verification via email. We ensure only genuine professionals receive job alerts.',
      color: 'text-green-600'
    },
    {
      icon: FiBell,
      number: '03',
      title: 'Start Receiving Alerts',
      description: 'Get curated job opportunities matching your skills delivered straight to your inbox.',
      color: 'text-purple-600'
    }
  ];

  return (
    <section id="how-it-works" className="section-padding bg-[rgb(var(--background))]">
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
            HOW IT WORKS
          </motion.p>
          <motion.h2
            variants={fadeInUp}
            className="text-3xl md:text-4xl font-bold mb-4"
          >
            Get Job Alerts in{' '}
            <span className="text-primary-600">3 Simple Steps</span>
          </motion.h2>
          <motion.p
            variants={fadeInUp}
            className="text-[rgb(var(--foreground))]/70 text-lg"
          >
            Start your journey to finding the perfect job opportunity
          </motion.p>
        </motion.div>

        {/* Steps Grid */}
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.2 }}
          variants={staggerContainer}
          className="grid md:grid-cols-3 gap-8 relative"
        >
          {/* Connecting Line - Desktop */}
          <div className="hidden md:block absolute top-24 left-0 right-0 h-0.5 bg-[rgb(var(--border))]">
            <div className="w-2/3 h-full bg-primary-600 mx-auto" />
          </div>

          {steps.map((step, index) => (
            <motion.div
              key={index}
              variants={fadeInUp}
              className="relative text-center"
            >
              {/* Step Number */}
              <div className="relative z-10">
                <div className="w-16 h-16 mx-auto mb-6 rounded-full bg-primary-600/10 flex items-center justify-center border-2 border-primary-600/20">
                  <step.icon className={`w-7 h-7 ${step.color}`} />
                </div>
                <p className="text-sm font-mono font-bold text-primary-600 mb-2">
                  {step.number}
                </p>
                <h3 className="text-xl font-semibold mb-3">
                  {step.title}
                </h3>
                <p className="text-[rgb(var(--foreground))]/70 text-sm leading-relaxed">
                  {step.description}
                </p>
              </div>

              {/* Arrow between steps - Desktop */}
              {index < steps.length - 1 && (
                <div className="hidden md:block absolute top-8 -right-4 text-[rgb(var(--foreground))]/20">
                  <FiArrowRight className="w-6 h-6" />
                </div>
              )}
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  );
};

export default HowItWorks;