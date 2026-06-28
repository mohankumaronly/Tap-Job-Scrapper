import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FiX, FiMail, FiArrowRight, FiCheckCircle } from 'react-icons/fi';
import OTPInput from './OTPInput';
import { authService } from '../../services/authService';

interface EmailModalProps {
    isOpen: boolean;
    onClose: () => void;
}

type Step = 'email' | 'otp' | 'success';

const EmailModal: React.FC<EmailModalProps> = ({ isOpen, onClose }) => {
    const [step, setStep] = useState<Step>('email');
    const [email, setEmail] = useState('');
    const [otp, setOtp] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSendOTP = async () => {
        if (!email) {
            setError('Please enter your email address');
            return;
        }
        if (!email.includes('@') || !email.includes('.')) {
            setError('Please enter a valid email address');
            return;
        }

        setIsLoading(true);
        setError('');

        try {
            const response = await authService.sendOTP(email);
            if (response.success) {
                setStep('otp');
            } else {
                setError(response.message || 'Failed to send OTP');
            }
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to send OTP. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    // ✅ Updated: Now accepts OTP as parameter
    const handleVerifyOTP = async (otpValue: string) => {
        // Use the passed OTP directly
        const trimmedOtp = otpValue.trim();

        // Log for debugging
        console.log('Verifying OTP:', trimmedOtp);
        console.log('OTP length:', trimmedOtp.length);

        if (trimmedOtp.length !== 6) {
            setError('Please enter a valid 6-digit OTP');
            return;
        }

        // Check if OTP contains only digits
        if (!/^\d{6}$/.test(trimmedOtp)) {
            setError('OTP must contain only numbers');
            return;
        }

        setIsLoading(true);
        setError('');

        try {
            const response = await authService.verifyOTP(email, trimmedOtp);
            console.log('Verification response:', response);

            if (response.success) {
                setStep('success');
            } else {
                setError(response.message || 'Invalid OTP. Please try again.');
            }
        } catch (err: any) {
            console.error('Verification error:', err);
            setError(err.response?.data?.message || 'Invalid OTP. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleClose = () => {
        setStep('email');
        setEmail('');
        setOtp('');
        setError('');
        onClose();
    };

    const handleResendOTP = async () => {
        setIsLoading(true);
        try {
            // ✅ Use actual resend API
            const response = await authService.sendOTP(email);
            if (response.success) {
                setError('OTP resent successfully!');
            } else {
                setError(response.message || 'Failed to resend OTP');
            }
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to resend OTP');
        } finally {
            setIsLoading(false);
        }
    };

    const backdropVariants = {
        hidden: { opacity: 0 },
        visible: { opacity: 1 }
    };

    const modalVariants = {
        hidden: { opacity: 0, scale: 0.95, y: 20 },
        visible: { opacity: 1, scale: 1, y: 0 }
    };

    const successVariants = {
        hidden: { opacity: 0, scale: 0.8 },
        visible: { opacity: 1, scale: 1 }
    };

    return (
        <AnimatePresence>
            {isOpen && (
                <>
                    {/* Backdrop */}
                    <motion.div
                        variants={backdropVariants}
                        initial="hidden"
                        animate="visible"
                        exit="hidden"
                        className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm"
                        onClick={handleClose}
                    />

                    {/* Modal */}
                    <motion.div
                        variants={modalVariants}
                        initial="hidden"
                        animate="visible"
                        exit="hidden"
                        transition={{ type: 'spring', damping: 25, stiffness: 300 }}
                        className="fixed inset-0 z-50 flex items-center justify-center p-4"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <div className="w-full max-w-md bg-[rgb(var(--background))] rounded-2xl border border-[rgb(var(--border))] shadow-2xl overflow-hidden">
                            {/* Close Button */}
                            <button
                                onClick={handleClose}
                                className="absolute top-4 right-4 p-1 rounded-lg hover:bg-[rgb(var(--border))] transition-colors duration-200"
                            >
                                <FiX className="w-5 h-5 text-[rgb(var(--foreground))]" />
                            </button>

                            <div className="p-6 md:p-8">
                                {/* Step 1: Email */}
                                {step === 'email' && (
                                    <motion.div
                                        initial={{ opacity: 0, x: -20 }}
                                        animate={{ opacity: 1, x: 0 }}
                                        exit={{ opacity: 0, x: 20 }}
                                    >
                                        <div className="text-center mb-6">
                                            <div className="w-14 h-14 mx-auto mb-4 rounded-full bg-primary-600/10 flex items-center justify-center">
                                                <FiMail className="w-7 h-7 text-primary-600" />
                                            </div>
                                            <h2 className="text-2xl font-bold">Get Started</h2>
                                            <p className="text-sm text-[rgb(var(--foreground))]/60 mt-1">
                                                Enter your email to receive job alerts
                                            </p>
                                        </div>

                                        <div className="space-y-4">
                                            <div>
                                                <label className="block text-sm font-medium mb-1.5">
                                                    Email Address
                                                </label>
                                                <input
                                                    type="email"
                                                    value={email}
                                                    onChange={(e) => {
                                                        setEmail(e.target.value);
                                                        setError('');
                                                    }}
                                                    placeholder="you@example.com"
                                                    className="w-full px-4 py-2.5 rounded-lg border border-[rgb(var(--border))] bg-[rgb(var(--background))] text-[rgb(var(--foreground))] focus:outline-none focus:ring-2 focus:ring-primary-600 transition-all duration-200"
                                                    onKeyDown={(e) => e.key === 'Enter' && handleSendOTP()}
                                                />
                                                {error && (
                                                    <p className="mt-1.5 text-sm text-red-500">{error}</p>
                                                )}
                                            </div>

                                            <button
                                                onClick={handleSendOTP}
                                                disabled={isLoading}
                                                className="w-full py-2.5 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors duration-200 flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
                                            >
                                                {isLoading ? (
                                                    <span className="flex items-center gap-2">
                                                        <span className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                                                        Sending...
                                                    </span>
                                                ) : (
                                                    <>
                                                        Send OTP
                                                        <FiArrowRight className="w-4 h-4" />
                                                    </>
                                                )}
                                            </button>
                                        </div>
                                    </motion.div>
                                )}

                                {/* Step 2: OTP */}
                                {step === 'otp' && (
                                    <motion.div
                                        initial={{ opacity: 0, x: -20 }}
                                        animate={{ opacity: 1, x: 0 }}
                                        exit={{ opacity: 0, x: 20 }}
                                    >
                                        <div className="text-center mb-6">
                                            <div className="w-14 h-14 mx-auto mb-4 rounded-full bg-primary-600/10 flex items-center justify-center">
                                                <FiCheckCircle className="w-7 h-7 text-primary-600" />
                                            </div>
                                            <h2 className="text-2xl font-bold">Verify Your Email</h2>
                                            <p className="text-sm text-[rgb(var(--foreground))]/60 mt-1">
                                                Enter the 6-digit code sent to{' '}
                                                <span className="font-medium text-[rgb(var(--foreground))]">
                                                    {email}
                                                </span>
                                            </p>
                                        </div>

                                        <div className="space-y-4">
                                            {/* ✅ OTPInput now passes OTP directly to onComplete */}
                                            <OTPInput
                                                value={otp}
                                                onChange={setOtp}
                                                onComplete={handleVerifyOTP}
                                            />

                                            {error && (
                                                <p className="text-sm text-red-500 text-center">{error}</p>
                                            )}

                                            <button
                                                onClick={() => handleVerifyOTP(otp)}
                                                disabled={isLoading || otp.length !== 6}
                                                className="w-full py-2.5 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                                            >
                                                {isLoading ? (
                                                    <span className="flex items-center justify-center gap-2">
                                                        <span className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                                                        Verifying...
                                                    </span>
                                                ) : (
                                                    'Verify OTP'
                                                )}
                                            </button>

                                            <div className="text-center">
                                                <button
                                                    onClick={handleResendOTP}
                                                    disabled={isLoading}
                                                    className="text-sm text-primary-600 hover:underline disabled:opacity-50"
                                                >
                                                    Resend OTP
                                                </button>
                                            </div>
                                        </div>
                                    </motion.div>
                                )}

                                {/* Step 3: Success */}
                                {step === 'success' && (
                                    <motion.div
                                        variants={successVariants}
                                        initial="hidden"
                                        animate="visible"
                                        className="text-center py-4"
                                    >
                                        <motion.div
                                            initial={{ scale: 0 }}
                                            animate={{ scale: 1 }}
                                            transition={{ type: 'spring', damping: 15, stiffness: 200 }}
                                            className="w-20 h-20 mx-auto mb-4 rounded-full bg-green-500/10 flex items-center justify-center"
                                        >
                                            <FiCheckCircle className="w-10 h-10 text-green-500" />
                                        </motion.div>

                                        <h2 className="text-2xl font-bold mb-2">You're All Set!</h2>
                                        <p className="text-[rgb(var(--foreground))]/60 text-sm mb-6">
                                            You'll receive job alerts at{' '}
                                            <span className="font-medium text-[rgb(var(--foreground))]">
                                                {email}
                                            </span>
                                        </p>

                                        <motion.div
                                            initial={{ opacity: 0, y: 10 }}
                                            animate={{ opacity: 1, y: 0 }}
                                            transition={{ delay: 0.3 }}
                                            className="space-y-3"
                                        >
                                            <div className="p-3 rounded-lg bg-primary-600/5 border border-primary-600/10">
                                                <p className="text-sm text-[rgb(var(--foreground))]/70">
                                                    🎉 You'll be notified when new internships open!
                                                </p>
                                            </div>
                                            <button
                                                onClick={handleClose}
                                                className="w-full py-2.5 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors duration-200"
                                            >
                                                Continue to Dashboard
                                            </button>
                                        </motion.div>
                                    </motion.div>
                                )}
                            </div>
                        </div>
                    </motion.div>
                </>
            )}
        </AnimatePresence>
    );
};

export default EmailModal;