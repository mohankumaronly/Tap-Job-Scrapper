import React from 'react';
import Navbar from './components/layout/Navbar';
import Hero from './components/sections/Hero';

function App() {
  return (
    <div className="min-h-screen bg-[rgb(var(--background))] text-[rgb(var(--foreground))]">
      <Navbar />
      <Hero />
    </div>
  );
}

export default App;