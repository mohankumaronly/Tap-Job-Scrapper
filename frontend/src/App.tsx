import React from 'react';
import Navbar from './components/layout/Navbar';
import Hero from './components/sections/Hero';
import HowItWorks from './components/sections/HowItWorks';
import Features from './components/sections/Features';

function App() {
  return (
    <div className="min-h-screen bg-[rgb(var(--background))] text-[rgb(var(--foreground))]">
      <Navbar />
      <Hero />
      <HowItWorks />
      <Features />
    </div>
  );
}

export default App;