import { useState } from 'react'
import HelloComponent from './components/HelloComponent'
import PaymentForm from './components/PaymentForm'
import PaymentsReport from './components/PaymentsReport'
import LoginForm from './components/LoginForm'

type Tab = 'hello' | 'payment' | 'report'

interface User {
  username: string
  password: string
  role: 'admin' | 'reporter'
}

function App() {
  const [activeTab, setActiveTab] = useState<Tab>('hello')
  const [user, setUser] = useState<User | null>(null)

  const handleLogin = (username: string, password: string, role: string) => {
    setUser({
      username,
      password,
      role: role as 'admin' | 'reporter'
    })
  }

  const handleLogout = () => {
    setUser(null)
    setActiveTab('hello')
  }

  if (!user) {
    return <LoginForm onLogin={handleLogin} />
  }

  const isAdmin = user.role === 'admin'
  const isReporter = user.role === 'reporter'

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-blue-600 text-white p-4">
        <div className="container mx-auto flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold">Spring Boot Payment UI</h1>
            <p className="text-sm text-blue-100">Logged in as: <strong>{user.username}</strong> ({user.role})</p>
          </div>
          <div className="flex gap-4 items-center">
            <button
              onClick={() => setActiveTab('hello')}
              className={`px-4 py-2 rounded ${
                activeTab === 'hello' ? 'bg-blue-800' : 'hover:bg-blue-700'
              }`}
            >
              Hello
            </button>
            {isAdmin && (
              <button
                onClick={() => setActiveTab('payment')}
                className={`px-4 py-2 rounded ${
                  activeTab === 'payment' ? 'bg-blue-800' : 'hover:bg-blue-700'
                }`}
              >
                Payment
              </button>
            )}
            {(isAdmin || isReporter) && (
              <button
                onClick={() => setActiveTab('report')}
                className={`px-4 py-2 rounded ${
                  activeTab === 'report' ? 'bg-blue-800' : 'hover:bg-blue-700'
                }`}
              >
                Report
              </button>
            )}
            <button
              onClick={handleLogout}
              className="px-4 py-2 bg-red-600 rounded hover:bg-red-700"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <main className="container mx-auto p-8">
        {activeTab === 'hello' && <HelloComponent />}
        {activeTab === 'payment' && isAdmin && <PaymentForm username={user.username} password={user.password} />}
        {activeTab === 'report' && (isAdmin || isReporter) && <PaymentsReport username={user.username} password={user.password} />}
      </main>
    </div>
  )
}

export default App
