import React, { useEffect } from 'react';
import { LoginButton } from '../component/LoginButton';
import { useIsAuthenticated } from "@azure/msal-react";
import { useAuth } from "react-oidc-context";
import { useNavigate } from 'react-router-dom';

export function LoginPage() {
    const isAuthenticatedAzure = useIsAuthenticated();
    const cognito = useAuth();
    const navigate = useNavigate();

    const isAuthenticated = isAuthenticatedAzure || cognito.isAuthenticated;
    useEffect(() => {
        if (isAuthenticated) {
            navigate('/dashboard');
        }
    }, [isAuthenticated, navigate]);

    return (
        <div style={{ padding: '60px', fontFamily: 'Arial, sans-serif', maxWidth: '500px', margin: '40px auto', textAlign: 'center', background: '#1e293b', color: 'white', borderRadius: '12px' }}>
            <h2>Iniciar Sesión</h2>
            <p style={{ color: '#94a3b8', marginBottom: '30px', fontSize: '14px' }}>
                Accede con tu cuenta corporativa (Azure Entra ID o AWS Cognito) para continuar.
            </p>
            <LoginButton />
        </div>
    );
}