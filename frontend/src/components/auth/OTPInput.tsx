import React, { useRef, useEffect, useState } from 'react';

interface OTPInputProps {
  value: string;
  onChange: (value: string) => void;
  onComplete?: (otp: string) => void; 
}

const OTPInput: React.FC<OTPInputProps> = ({ value, onChange, onComplete }) => {
  const [focusedIndex, setFocusedIndex] = useState<number | null>(null);
  const inputRefs = useRef<(HTMLInputElement | null)[]>([]);
  const otpLength = 6;

  // Initialize refs array
  useEffect(() => {
    inputRefs.current = inputRefs.current.slice(0, otpLength);
  }, [otpLength]);

  useEffect(() => {
    // Focus first empty input
    if (inputRefs.current[0]) {
      const firstEmptyIndex = value.split('').findIndex(char => char === '');
      const focusIndex = firstEmptyIndex === -1 ? otpLength - 1 : firstEmptyIndex;
      if (focusIndex >= 0 && focusIndex < otpLength) {
        inputRefs.current[focusIndex]?.focus();
      }
    }
  }, []);

  const handleChange = (index: number, char: string) => {
    // Only allow digits
    if (char !== '' && !/^\d$/.test(char)) return;

    // Create new OTP array
    const otpArray = value.split('');
    otpArray[index] = char;
    const newOtp = otpArray.join('');

    // Update parent state
    onChange(newOtp);

    // Auto-advance to next input if a digit was entered
    if (char && index < otpLength - 1) {
      setTimeout(() => {
        inputRefs.current[index + 1]?.focus();
      }, 50);
    }

    // Auto-submit on complete - ✅ Pass OTP value directly
    if (newOtp.length === otpLength && onComplete) {
      setTimeout(() => {
        onComplete(newOtp); // ✅ Pass the OTP value directly
      }, 150);
    }
  };

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    const currentValue = value[index] || '';

    if (e.key === 'Backspace') {
      if (currentValue) {
        // Clear current field
        const otpArray = value.split('');
        otpArray[index] = '';
        onChange(otpArray.join(''));
      } else if (index > 0) {
        // Move to previous field
        inputRefs.current[index - 1]?.focus();
      }
      e.preventDefault();
    }

    if (e.key === 'ArrowLeft' && index > 0) {
      inputRefs.current[index - 1]?.focus();
      e.preventDefault();
    }

    if (e.key === 'ArrowRight' && index < otpLength - 1) {
      inputRefs.current[index + 1]?.focus();
      e.preventDefault();
    }

    if (e.key === 'Enter' && onComplete && value.length === otpLength) {
      onComplete(value); // ✅ Pass value directly on Enter
    }
  };

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text/plain').trim();
    if (/^\d{6}$/.test(pastedData)) {
      onChange(pastedData);
      if (onComplete) {
        setTimeout(() => {
          onComplete(pastedData); // ✅ Pass pasted OTP directly
        }, 150);
      }
    }
  };

  // Set ref function
  const setInputRef = (index: number) => (el: HTMLInputElement | null) => {
    inputRefs.current[index] = el;
  };

  // Handle focus on input
  const handleFocus = (index: number) => {
    setFocusedIndex(index);
    inputRefs.current[index]?.select();
  };

  // Get OTP array for rendering
  const otpArray = Array.from({ length: otpLength }, (_, i) => value[i] || '');

  return (
    <div className="flex justify-center gap-2 md:gap-3" onPaste={handlePaste}>
      {Array.from({ length: otpLength }).map((_, index) => (
        <input
          key={index}
          ref={setInputRef(index)}
          type="text"
          maxLength={1}
          value={otpArray[index]}
          onChange={(e) => handleChange(index, e.target.value)}
          onKeyDown={(e) => handleKeyDown(index, e)}
          onFocus={() => handleFocus(index)}
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