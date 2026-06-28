import React, { useRef, useEffect, useState } from 'react';

interface OTPInputProps {
  value: string;
  onChange: (value: string) => void;
  onComplete?: () => void;
}

const OTPInput: React.FC<OTPInputProps> = ({ value, onChange, onComplete }) => {
  const [focusedIndex, setFocusedIndex] = useState<number | null>(null);
  const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

  const otpLength = 6;
  const otpArray = value.padEnd(otpLength, '').split('');

  // Initialize refs array
  useEffect(() => {
    inputRefs.current = inputRefs.current.slice(0, otpLength);
  }, [otpLength]);

  useEffect(() => {
    // Focus first empty input or last filled input
    if (inputRefs.current[0]) {
      const firstEmptyIndex = otpArray.findIndex(char => char === '');
      const focusIndex = firstEmptyIndex === -1 ? otpLength - 1 : firstEmptyIndex;
      if (focusIndex >= 0 && focusIndex < otpLength) {
        inputRefs.current[focusIndex]?.focus();
      }
    }
  }, []);

  const handleChange = (index: number, char: string) => {
    if (!/^\d*$/.test(char) && char !== '') return;

    const newOtp = otpArray.map((c, i) => (i === index ? char : c)).join('');
    onChange(newOtp);

    // Auto-advance to next input
    if (char && index < otpLength - 1) {
      inputRefs.current[index + 1]?.focus();
    }

    // Auto-submit on complete
    if (newOtp.length === otpLength && onComplete) {
      setTimeout(onComplete, 300);
    }
  };

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !otpArray[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
    if (e.key === 'ArrowLeft' && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
    if (e.key === 'ArrowRight' && index < otpLength - 1) {
      inputRefs.current[index + 1]?.focus();
    }
    if (e.key === 'Enter' && onComplete && value.length === otpLength) {
      onComplete();
    }
  };

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text/plain').trim();
    if (/^\d{6}$/.test(pastedData)) {
      onChange(pastedData);
      if (onComplete) {
        setTimeout(onComplete, 300);
      }
    }
  };

  // Set ref function - returns void
  const setInputRef = (index: number) => (el: HTMLInputElement | null) => {
    inputRefs.current[index] = el;
  };

  return (
    <div className="flex justify-center gap-2 md:gap-3" onPaste={handlePaste}>
      {Array.from({ length: otpLength }).map((_, index) => (
        <input
          key={index}
          ref={setInputRef(index)}
          type="text"
          maxLength={1}
          value={otpArray[index] || ''}
          onChange={(e) => handleChange(index, e.target.value)}
          onKeyDown={(e) => handleKeyDown(index, e)}
          onFocus={() => setFocusedIndex(index)}
          onBlur={() => setFocusedIndex(null)}
          className={`w-10 h-12 md:w-12 md:h-14 text-center text-lg font-semibold rounded-lg border-2 transition-all duration-200 outline-none
            ${otpArray[index] 
              ? 'border-primary-600 bg-primary-600/5' 
              : 'border-[rgb(var(--border))] bg-[rgb(var(--background))]'
            }
            ${focusedIndex === index ? 'ring-2 ring-primary-600 border-primary-600' : ''}
            text-[rgb(var(--foreground))]
          `}
          inputMode="numeric"
          autoComplete="off"
        />
      ))}
    </div>
  );
};

export default OTPInput;