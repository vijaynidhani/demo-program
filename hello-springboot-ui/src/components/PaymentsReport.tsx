import { useState, useEffect } from 'react'
import { apiClient } from '../services/api'

interface PaymentsReportProps {
  username: string
  password: string
}

interface PaymentRow {
  timestamp: string
  idempotencyKey: string
  amount: string
  fromAccount: string
  toAccount: string
  name: string
}

export default function PaymentsReport({ username, password }: PaymentsReportProps) {
  const [payments, setPayments] = useState<PaymentRow[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetchReport()
  }, [])

  const fetchReport = async () => {
    try {
      setLoading(true)
      setError('')
      const htmlResponse = await apiClient.getPaymentsReport(username, password)
      
      // Parse the HTML table from the response
      const rows = parseHtmlTable(htmlResponse)
      setPayments(rows)
    } catch (err) {
      setError('Failed to fetch payment report')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const parseHtmlTable = (html: string): PaymentRow[] => {
    const parser = new DOMParser()
    const doc = parser.parseFromString(html, 'text/html')
    const rows: PaymentRow[] = []

    const tableRows = doc.querySelectorAll('table tbody tr')
    tableRows.forEach((row) => {
      const cells = row.querySelectorAll('td')
      if (cells.length >= 7) {
        rows.push({
          timestamp: cells[1]?.textContent?.trim() || '',
          idempotencyKey: cells[2]?.textContent?.trim() || '',
          amount: cells[3]?.textContent?.trim() || '',
          toAccount: cells[4]?.textContent?.trim() || '',
          fromAccount: cells[5]?.textContent?.trim() || '',
          name: cells[6]?.textContent?.trim() || '',
        })
      }
    })

    return rows
  }

  const getTotalAmount = () => {
    return payments
      .reduce((sum, payment) => {
        const amount = parseFloat(payment.amount.replace(/[$,]/g, ''))
        return sum + (isNaN(amount) ? 0 : amount)
      }, 0)
      .toFixed(2)
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-800">Payment Report</h2>
        <button
          onClick={fetchReport}
          disabled={loading}
          className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? 'Refreshing...' : 'Refresh'}
        </button>
      </div>

      {loading && <p className="text-gray-600">Loading report...</p>}
      
      {error && (
        <div className="bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded-lg">
          {error}
        </div>
      )}

      {!loading && !error && payments.length > 0 && (
        <>
          <div className="bg-blue-50 p-4 rounded-lg border-l-4 border-blue-600">
            <p className="text-blue-900">
              <strong>Total Payments:</strong> {payments.length} | <strong>Total Amount:</strong> ${getTotalAmount()}
            </p>
          </div>

          <div className="bg-white rounded-lg shadow-lg overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-800 text-white">
                  <tr>
                    <th className="px-4 py-3 text-left">Timestamp</th>
                    <th className="px-4 py-3 text-left">Idempotency Key</th>
                    <th className="px-4 py-3 text-right">Amount</th>
                    <th className="px-4 py-3 text-left">From Account</th>
                    <th className="px-4 py-3 text-left">To Account</th>
                    <th className="px-4 py-3 text-left">Recipient Name</th>
                  </tr>
                </thead>
                <tbody>
                  {payments.map((payment, index) => (
                    <tr key={index} className={index % 2 === 0 ? 'bg-gray-50' : 'bg-white'}>
                      <td className="px-4 py-3 text-gray-800 text-sm">{payment.timestamp}</td>
                      <td className="px-4 py-3 text-gray-600 text-sm font-mono">{payment.idempotencyKey}</td>
                      <td className="px-4 py-3 text-gray-800 font-semibold text-right">{payment.amount}</td>
                      <td className="px-4 py-3 text-gray-800 text-sm">{payment.fromAccount}</td>
                      <td className="px-4 py-3 text-gray-800 text-sm">{payment.toAccount}</td>
                      <td className="px-4 py-3 text-gray-800">{payment.name}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}

      {!loading && !error && payments.length === 0 && (
        <div className="bg-yellow-50 border border-yellow-300 text-yellow-700 px-4 py-3 rounded-lg">
          No payments found in the report.
        </div>
      )}
    </div>
  )
}
