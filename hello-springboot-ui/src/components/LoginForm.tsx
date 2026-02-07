import { useState } from 'react'

interface LoginFormProps {
  onLogin: (username: string, password: string, role: string) => void
}

export default function LoginForm({ onLogin }: LoginFormProps) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [selectedRole, setSelectedRole] = useState<'admin' | 'reporter'>('admin')

  const handleQuickLogin = (role: 'admin' | 'reporter', user: string) => {
    setSelectedRole(role)
    onLogin(user, user, role)
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (username && password) {
      const role = username === 'admin' ? 'admin' : 'reporter'
      onLogin(username, password, role)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-600 to-blue-800 flex items-center justify-center">
      <div className="bg-white p-8 rounded-lg shadow-lg w-96">
        <h1 className="text-3xl font-bold text-center mb-8 text-gray-800">Payment System</h1>
        
        <div className="mb-8">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">Quick Login</h2>
          <button
            onClick={() => handleQuickLogin('admin', 'admin')}
            className="w-full mb-3 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 font-bold transition"
          >
            Login as Admin
          </button>
          <button
            onClick={() => handleQuickLogin('reporter', 'reporter')}
            className="w-full mb-3 bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 font-bold transition"
          >
            Login as Reporter
          </button>
          <button
            onClick={() => handleQuickLogin('reporter', 'vijay')}
            className="w-full bg-purple-600 text-white py-2 rounded-lg hover:bg-purple-700 font-bold transition"
          >
            Login as Vijay (Reporter)
          </button>
        </div>

        <div className="relative mb-6">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-gray-300"></div>
          </div>
          <div className="relative flex justify-center text-sm">
            <span className="px-2 bg-white text-gray-500">or</span>
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-600"
              placeholder="admin, reporter, or vijay"
            />
          </div>
          <div className="mb-6">
            <label className="block text-gray-700 text-sm font-bold mb-2">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-600"
              placeholder="password"
            />
          </div>
          <button
            type="submit"
            disabled={!username || !password}
            className="w-full bg-gray-600 text-white py-2 rounded-lg hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed font-bold"
          >
            Custom Login
          </button>
        </form>

        <div className="mt-6 bg-blue-50 p-4 rounded-lg border border-blue-200">
          <h3 className="font-bold text-blue-900 mb-2">Available Credentials:</h3>
          <div className="text-sm text-blue-800 space-y-1">
            <p><strong>Admin:</strong> admin / admin (full access)</p>
            <p><strong>Reporter:</strong> reporter / reporter (view only)</p>
            <p><strong>Reporter:</strong> vijay / vijay (view only)</p>
          </div>
        </div>
      </div>
    </div>
  )
}
