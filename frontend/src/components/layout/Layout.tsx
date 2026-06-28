import React from 'react';
import Navbar from './Navbar';
import Hero from '../sections/Hero';
import HowItWorks from '../sections/HowItWorks';
import Features from '../sections/Features';
import About from '../sections/About';
import FAQ from '../sections/FAQ';
import Footer from '../sections/Footer';

const Layout: React.FC = () => {
  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
      <main className="flex-grow">
        <Hero />
        <HowItWorks />
        <Features />
        <About />
        <FAQ />
      </main>
      <Footer />
    </div>
  );
};

export default Layout;