import React from 'react';
import { motion } from 'framer-motion';
import { 
  FiBriefcase, 
  FiGithub, 
  FiTwitter, 
  FiLinkedin, 
  FiMail,
  FiMapPin,
  FiSend,
} from 'react-icons/fi';

const Footer: React.FC = () => {
  const currentYear = new Date().getFullYear();

  const fadeInUp = {
    hidden: { opacity: 0, y: 20 },
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

  const footerLinks = {
    product: [
      { label: 'Features', href: '#features' },
      { label: 'How It Works', href: '#how-it-works' },
      { label: 'Pricing', href: '#' },
      { label: 'Integrations', href: '#' }
    ],
    company: [
      { label: 'About', href: '#about' },
      { label: 'Blog', href: '#' },
      { label: 'Careers', href: '#' },
      { label: 'Contact', href: '#' }
    ],
    support: [
      { label: 'FAQ', href: '#faq' },
      { label: 'Privacy Policy', href: '#' },
      { label: 'Terms of Service', href: '#' },
      { label: 'Cookie Policy', href: '#' }
    ]
  };

  const socialLinks = [
    { icon: FiGithub, label: 'GitHub', href: '#' },
    { icon: FiTwitter, label: 'Twitter', href: '#' },
    { icon: FiLinkedin, label: 'LinkedIn', href: '#' },
    { icon: FiMail, label: 'Email', href: '#' }
  ];

  return (
    <footer className="bg-[rgb(var(--background))] border-t border-[rgb(var(--border))]">
      <div className="container-custom">
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.3 }}
          variants={staggerContainer}
          className="py-12 md:py-16"
        >
          {/* Main Footer Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-8 md:gap-12">
            {/* Brand Column */}
            <motion.div
              variants={fadeInUp}
              className="lg:col-span-2"
            >
              <div className="flex items-center gap-2 mb-4">
                <FiBriefcase className="w-6 h-6 text-primary-600" />
                <span className="text-xl font-bold text-[rgb(var(--foreground))]">
                  JobAlert
                </span>
              </div>
              <p className="text-sm text-[rgb(var(--foreground))]/60 mb-4 max-w-sm leading-relaxed">
                Smart job alerts for Tap Academy students and interns. Never miss 
                an internship opportunity again.
              </p>
              <div className="flex items-center gap-2 text-sm text-[rgb(var(--foreground))]/60">
                <FiMapPin className="w-4 h-4" />
                <span>Tap Academy, India</span>
              </div>
            </motion.div>

            {/* Product Links */}
            <motion.div variants={fadeInUp}>
              <h3 className="text-sm font-semibold text-[rgb(var(--foreground))] mb-4">
                Product
              </h3>
              <ul className="space-y-2.5">
                {footerLinks.product.map((link) => (
                  <li key={link.label}>
                    <a
                      href={link.href}
                      className="text-sm text-[rgb(var(--foreground))]/60 hover:text-primary-600 transition-colors duration-200"
                    >
                      {link.label}
                    </a>
                  </li>
                ))}
              </ul>
            </motion.div>

            {/* Company Links */}
            <motion.div variants={fadeInUp}>
              <h3 className="text-sm font-semibold text-[rgb(var(--foreground))] mb-4">
                Company
              </h3>
              <ul className="space-y-2.5">
                {footerLinks.company.map((link) => (
                  <li key={link.label}>
                    <a
                      href={link.href}
                      className="text-sm text-[rgb(var(--foreground))]/60 hover:text-primary-600 transition-colors duration-200"
                    >
                      {link.label}
                    </a>
                  </li>
                ))}
              </ul>
            </motion.div>

            {/* Support Links */}
            <motion.div variants={fadeInUp}>
              <h3 className="text-sm font-semibold text-[rgb(var(--foreground))] mb-4">
                Support
              </h3>
              <ul className="space-y-2.5">
                {footerLinks.support.map((link) => (
                  <li key={link.label}>
                    <a
                      href={link.href}
                      className="text-sm text-[rgb(var(--foreground))]/60 hover:text-primary-600 transition-colors duration-200"
                    >
                      {link.label}
                    </a>
                  </li>
                ))}
              </ul>
            </motion.div>
          </div>

          {/* Newsletter Section */}
          <motion.div
            variants={fadeInUp}
            className="mt-12 pt-8 border-t border-[rgb(var(--border))]"
          >
            <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
              <div>
                <h4 className="text-sm font-semibold text-[rgb(var(--foreground))] mb-1">
                  Subscribe to our newsletter
                </h4>
                <p className="text-sm text-[rgb(var(--foreground))]/60">
                  Get the latest internship alerts and updates
                </p>
              </div>
              <div className="flex w-full md:w-auto gap-2">
                <input
                  type="email"
                  placeholder="Enter your email"
                  className="flex-1 md:w-64 px-4 py-2 text-sm rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))] text-[rgb(var(--foreground))] focus:outline-none focus:ring-2 focus:ring-primary-600"
                />
                <button className="px-4 py-2 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors duration-200 flex items-center gap-2 whitespace-nowrap">
                  Subscribe
                  <FiSend className="w-4 h-4" />
                </button>
              </div>
            </div>
          </motion.div>

          {/* Bottom Bar */}
          <motion.div
            variants={fadeInUp}
            className="mt-8 pt-6 border-t border-[rgb(var(--border))] flex flex-col sm:flex-row items-center justify-between gap-4"
          >
            <p className="text-sm text-[rgb(var(--foreground))]/40">
              © {currentYear} JobAlert. All rights reserved. Made with ❤️ for Tap Academy students
            </p>
            
            <div className="flex items-center gap-4">
              {socialLinks.map((social) => (
                <a
                  key={social.label}
                  href={social.href}
                  aria-label={social.label}
                  className="p-2 rounded-lg text-[rgb(var(--foreground))]/40 hover:text-primary-600 hover:bg-[rgb(var(--border))] transition-all duration-200"
                >
                  <social.icon className="w-4 h-4" />
                </a>
              ))}
            </div>
          </motion.div>
        </motion.div>
      </div>
    </footer>
  );
};

export default Footer;