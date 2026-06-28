import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FiPlus, FiMinus, FiSearch } from 'react-icons/fi';

interface FAQItem {
  question: string;
  answer: string;
  category: string;
}

const FAQ: React.FC = () => {
  const [openIndex, setOpenIndex] = useState<number | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [activeCategory, setActiveCategory] = useState('All');

  const faqs: FAQItem[] = [
    {
      question: 'How do I receive job alerts?',
      answer: 'You\'ll receive email notifications whenever a new internship or job opportunity that matches your preferences is posted. Simply enter your email address and verify with OTP to start receiving alerts.',
      category: 'General'
    },
    {
      question: 'Is the service free?',
      answer: 'Yes, JobAlert is completely free for all Tap Academy students and interns. We believe in making career opportunities accessible to everyone.',
      category: 'General'
    },
    {
      question: 'How can I unsubscribe?',
      answer: 'You can unsubscribe anytime by clicking the "Unsubscribe" link at the bottom of any email alert. You can also manage your preferences from your dashboard.',
      category: 'Account'
    },
    {
      question: 'What job portals do you support?',
      answer: 'We integrate with major job portals including LinkedIn, Indeed, Naukri, and various company career pages to bring you the most comprehensive opportunities.',
      category: 'Technical'
    },
    {
      question: 'How often will I receive alerts?',
      answer: 'You can customize the frequency of alerts in your preferences. Options include instant notifications, daily digest, or weekly summaries.',
      category: 'Preferences'
    },
    {
      question: 'Is my data secure?',
      answer: 'We use industry-standard encryption and security practices to protect your data. We never share your information with third parties without your consent.',
      category: 'Security'
    },
    {
      question: 'Can I pause notifications?',
      answer: 'Yes, you can temporarily pause notifications from your dashboard settings. Perfect for when you\'re on vacation or focusing on exams.',
      category: 'Preferences'
    },
    {
      question: 'Do you have a mobile app?',
      answer: 'A mobile app is currently in development and will be available soon. Stay tuned for updates!',
      category: 'General'
    },
    {
      question: 'What types of jobs are posted?',
      answer: 'We focus on internships, entry-level positions, and fresher roles specifically suitable for Tap Academy students. All opportunities are verified.',
      category: 'Technical'
    },
    {
      question: 'How do I update my preferences?',
      answer: 'You can update your job preferences, frequency, and email settings anytime from your profile dashboard after logging in.',
      category: 'Account'
    }
  ];

  const categories = ['All', ...new Set(faqs.map(faq => faq.category))];

  const filteredFaqs = faqs.filter(faq => {
    const matchesSearch = faq.question.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         faq.answer.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = activeCategory === 'All' || faq.category === activeCategory;
    return matchesSearch && matchesCategory;
  });

  const toggleFAQ = (index: number) => {
    setOpenIndex(openIndex === index ? null : index);
  };

  return (
    <section id="faq" className="section-padding bg-[rgb(var(--background))]">
      <div className="container-custom">
        {/* Section Header */}
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.3 }}
          variants={{
            hidden: { opacity: 0 },
            visible: {
              opacity: 1,
              transition: {
                staggerChildren: 0.15
              }
            }
          }}
          className="text-center max-w-3xl mx-auto mb-12"
        >
          <motion.p
            variants={{
              hidden: { opacity: 0, y: 20 },
              visible: { opacity: 1, y: 0 }
            }}
            className="text-sm font-medium text-primary-600 mb-3"
          >
            FAQ
          </motion.p>
          <motion.h2
            variants={{
              hidden: { opacity: 0, y: 20 },
              visible: { opacity: 1, y: 0 }
            }}
            className="text-3xl md:text-4xl font-bold mb-4"
          >
            Frequently Asked{' '}
            <span className="text-primary-600">Questions</span>
          </motion.h2>
          <motion.p
            variants={{
              hidden: { opacity: 0, y: 20 },
              visible: { opacity: 1, y: 0 }
            }}
            className="text-[rgb(var(--foreground))]/70 text-lg"
          >
            Find answers to common questions about JobAlert
          </motion.p>
        </motion.div>

        {/* Search and Filter */}
        <div className="max-w-3xl mx-auto mb-8">
          <div className="flex flex-col sm:flex-row gap-4">
            {/* Search Bar */}
            <div className="flex-1 relative">
              <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-[rgb(var(--foreground))]/40" />
              <input
                type="text"
                placeholder="Search questions..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))] text-[rgb(var(--foreground))] focus:outline-none focus:ring-2 focus:ring-primary-600"
              />
            </div>

            {/* Category Filter */}
            <div className="flex gap-2 overflow-x-auto pb-2 sm:pb-0">
              {categories.map((category) => (
                <button
                  key={category}
                  onClick={() => setActiveCategory(category)}
                  className={`px-3 py-1.5 text-sm rounded-lg whitespace-nowrap transition-colors duration-200 ${
                    activeCategory === category
                      ? 'bg-primary-600 text-white'
                      : 'border border-[rgb(var(--border))] text-[rgb(var(--foreground))]/70 hover:bg-[rgb(var(--border))]'
                  }`}
                >
                  {category}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* FAQ Accordion */}
        <div className="max-w-3xl mx-auto">
          {filteredFaqs.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-[rgb(var(--foreground))]/70">No questions found matching your search.</p>
            </div>
          ) : (
            <div className="space-y-3">
              {filteredFaqs.map((faq, index) => {
                const actualIndex = faqs.indexOf(faq);
                const isOpen = openIndex === actualIndex;
                
                return (
                  <motion.div
                    key={actualIndex}
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: index * 0.05 }}
                    className="border border-[rgb(var(--border))] rounded-lg overflow-hidden"
                  >
                    <button
                      onClick={() => toggleFAQ(actualIndex)}
                      className="w-full px-4 py-4 flex items-start justify-between text-left hover:bg-[rgb(var(--border))]/30 transition-colors duration-200"
                    >
                      <span className="text-sm md:text-base font-medium pr-4">
                        {faq.question}
                      </span>
                      <span className="flex-shrink-0 mt-0.5">
                        {isOpen ? (
                          <FiMinus className="w-5 h-5 text-primary-600" />
                        ) : (
                          <FiPlus className="w-5 h-5 text-primary-600" />
                        )}
                      </span>
                    </button>
                    
                    <AnimatePresence>
                      {isOpen && (
                        <motion.div
                          initial={{ height: 0, opacity: 0 }}
                          animate={{ height: 'auto', opacity: 1 }}
                          exit={{ height: 0, opacity: 0 }}
                          transition={{ duration: 0.3 }}
                          className="overflow-hidden"
                        >
                          <div className="px-4 pb-4 pt-1 border-t border-[rgb(var(--border))]">
                            <p className="text-sm text-[rgb(var(--foreground))]/70 leading-relaxed">
                              {faq.answer}
                            </p>
                            <span className="inline-block mt-2 text-xs text-primary-600 bg-primary-600/10 px-2 py-0.5 rounded">
                              {faq.category}
                            </span>
                          </div>
                        </motion.div>
                      )}
                    </AnimatePresence>
                  </motion.div>
                );
              })}
            </div>
          )}
        </div>

        {/* Still Have Questions */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView="visible"
          viewport={{ once: true }}
          className="text-center mt-12 p-6 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))]"
        >
          <p className="text-[rgb(var(--foreground))]/70 text-sm">
            Still have questions? {' '}
            <button className="text-primary-600 font-medium hover:underline">
              Contact us
            </button>
          </p>
        </motion.div>
      </div>
    </section>
  );
};

export default FAQ;