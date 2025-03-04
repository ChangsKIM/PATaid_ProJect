import React from 'react';
import Header from '../components/layout/Header';
import Footer from '../components/layout/Footer';
import { Link } from 'react-router-dom';

function HomePage() {
  return (
    <>
      {/* 상단 Header */}
      <Header />

      {/* 중앙부 (2×2 레이아웃) */}
      <main style={{ padding: '16px' }}>
        <h2>메인 페이지</h2>

        <div style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: '16px'
        }}>
          {/* 첫 번째 카드 */}
          <div style={{ border: '1px solid #ccc', padding: '16px' }}>
            <h3>선행 기술조사 보고서 작성</h3>
            <p>선행기술을 조사하고 보고서를 작성합니다.</p>
            <Link to="/prior-art">바로가기</Link>
          </div>

          {/* 두 번째 카드 */}
          <div style={{ border: '1px solid #ccc', padding: '16px' }}>
            <h3>특허 명세서 작성</h3>
            <p>특허 명세서를 AI로 작성해보세요.</p>
            <Link to="/patent">바로가기</Link>
          </div>

          {/* 세 번째 카드 */}
          <div style={{ border: '1px solid #ccc', padding: '16px' }}>
            <h3>특허 의견서 작성</h3>
            <p>거절이유통지에 대한 의견서를 손쉽게 작성합니다.</p>
            <Link to="/opinion">바로가기</Link>
          </div>

          {/* 네 번째 카드 */}
          <div style={{ border: '1px solid #ccc', padding: '16px' }}>
            <h3>사용방법</h3>
            <p>시나리오에 따른 사용방법 안내</p>
            <Link to="/help">바로가기</Link>
          </div>
        </div>
      </main>

      {/* 하단 Footer */}
      <Footer />
    </>
  );
}

export default HomePage;
