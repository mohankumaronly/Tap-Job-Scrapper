import React from 'react';
import Navbar from './components/layout/Navbar';

function App() {
  return (
    <div className="min-h-screen bg-[rgb(var(--background))] text-[rgb(var(--foreground))]">
      <Navbar />
      <div className="flex items-center justify-center min-h-screen pt-16">
        <div className="text-center">
          <h1 className="text-4xl md:text-6xl font-bold mb-4">
            Job Alert Landing Page
          </h1>
          <p className="text-[rgb(var(--foreground))]/70 text-lg">
            Navbar with theme toggle is working!
          </p>
        </div>
      </div>
    </div>
  );
}

export default App;