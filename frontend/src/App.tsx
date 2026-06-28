import React from 'react';
import Navbar from './components/layout/Navbar';
import Hero from './components/sections/Hero';
import HowItWorks from './components/sections/HowItWorks';
import Features from './components/sections/Features';
import About from './components/sections/About';
import FAQ from './components/sections/FAQ';
import Footer from './components/sections/Footer';

function App() {
  return (
    <div className="min-h-screen bg-[rgb(var(--background))] text-[rgb(var(--foreground))]">
      <Navbar />
      <Hero />
      <HowItWorks />
      <Features />
      <About />
      <FAQ />
      <Footer />
    </div>
  );
}

export default App;