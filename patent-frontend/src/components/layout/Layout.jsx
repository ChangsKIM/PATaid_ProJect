// src/components/layout/Layout.jsx
import React from 'react';
import Header from './Header';
import Footer from './Footer';
import '../../css/Layout.css'; // Layout 전용 CSS

function Layout({ children }) {
  return (
    <div className="layout-container">
      <Header />
      <main className="layout-main">
        {children}
      </main>
      <Footer />
    </div>
  );
}

export default Layout;
