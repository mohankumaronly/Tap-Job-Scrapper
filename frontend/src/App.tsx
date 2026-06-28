import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './components/layout/Layout';
import Navbar from './components/layout/Navbar';
import Jobs from './pages/Jobs';
import ScrollToTop from './components/ScrollToTop';
import Footer from './components/sections/Footer';

function App() {
  return (
    <BrowserRouter>
      <ScrollToTop />
      <div className="min-h-screen bg-[rgb(var(--background))] text-[rgb(var(--foreground))]">
        <Routes>
          <Route path="/" element={<Layout />} />
          <Route path="/jobs" element={
            <>
              <Navbar />
              <Jobs />
              <Footer />
            </>
          } />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;