let divisions = []
let employees = []
let numOfWeeks = 0
let weeks = {}
let numOfWeek_evenOdd = {}
function modal(data) {

    const modal = document.getElementById("modal")
    modal.style.display = "block"
    const modal_text = modal.querySelector("div.modal__wrapper")
    modal_text.innerHTML = ''

    data.ID.forEach((elementData, index) => {
        if (data["Postponed"][index] === true) {
            modal_text.innerHTML += `
        <div><b>Фактическая(перенесенная) дата начала отпуска:</b> ${data["PostponedStartDate"][index]}</div>
        <div><b>Фактическая(перенесенная) дата окончания отпуска:</b> ${data["PostponedEndDate"][index]}</div>
        <div class='semi-transparent'><b>Дата начала отпуска по графику:</b> ${data["StartDate"][index]}</div>
        <div class='semi-transparent'><b>Дата окончания отпуска по графику:</b> ${data["EndDate"][index]}</div>
      `
        } else {
            modal_text.innerHTML += `
        <div><b>Дата начала отпуска по графику:</b> ${data["StartDate"][index]}</div>
        <div><b>Дата окончания отпуска по графику:</b> ${data["EndDate"][index]}</div>
      `
        }
        modal_text.innerHTML += `
      <div><b>Отпуск перенесен:</b> ${(data["Postponed"][index])===true ? "Да" : "Нет"}</div>
      <div><b>Документ планирования:</b> ${data["ThePlanningDocument"][index]}</div>
      <div><b>Документ переноса:</b> ${data["TransferDocument"][index]}</div>
      <div><b>Кол-во дней запланировано:</b> ${data["NumberOfDays"][index]}</div>
      <div><b>Кол-во дней перенесено:</b> ${data["RescheduledQuantityToday"][index]}</div>
      <div><b>Вид отпуска:</b> ${data["TypeOfAccess"][index]}</div>
      <div><b>Примечание:</b> ${data["Note"][index]}</div>
      <div><b>Дней запланировано:</b> ${data["PlanOfDays"][index]}</div>
      <div><b>Дней перенесено:</b> ${data["PostponedDays"][index]}</div>
    `
        let checkDatesResult;
        if (data["Postponed"][index] === true) {
            checkDatesResult = filterDatesInPeriod(
                data["Holiday"][index],
                data["PostponedStartDate"][index],
                data["PostponedEndDate"][index]
            );
        } else {
            checkDatesResult = filterDatesInPeriod(
                data["Holiday"][index],
                data["StartDate"][index],
                data["EndDate"][index]
            );
        }
        if (checkDatesResult.count > 0) {
            modal_text.innerHTML += `
        <div><b>Праздничных дней в период отпуска:</b> ${checkDatesResult.count} (${checkDatesResult.dates}) </div>
      `
        }
        // split vacantions
        if (index < data.ID.length - 1) {
            modal_text.innerHTML += `
        <div class='modal-divider'></div>
      `

        }
    });
    modal_text.innerHTML += `
    <img class="modal-close" src="/vacation/assets/modal-close.svg" alt="">
  `
    window.addEventListener("keydown", e => {
        if (e.key === "Escape") {
            hideModal(modal)
        }
    })
    const closeModalHandler = document.querySelectorAll('.modal-close');
    closeModalHandler.forEach(closer => {
        closer.addEventListener('click', () => hideModal(modal))
    });
    function hideModal(modal) {
        modal.style.display = "none"
    }
}
// function getKeyByValue(object, key, value) {
//   return Object.keys(object).find(key => {
//     if (key == "smth") {
//       return object[key] === value
//     }
//   });
// }
function getKeyByValue(object, value) {
    return Object.keys(object).find(key => object[key] === value);
}
const parseDivisions = (data, idx = 0) => {
    for (entry of data) {
        if (idx !== 0) {
            divisions.push({
                "Division": entry["selfDivison"],
                "ID": entry["DivisionID"],
                "LVL": idx
            })
        }
        if (entry["Division"] == []) {
            return
        }
        parseDivisions(entry["Division"], idx + 1)
    }
}
const MONTHS = {
    "01": "Январь",
    "02": "Февраль",
    "03": "Март",
    "04": "Апрель",
    "05": "Май",
    "06": "Июнь",
    "07": "Июль",
    "08": "Август",
    "09": "Сентябрь",
    "10": "Октябрь",
    "11": "Ноябрь",
    "12": "Декабрь",
}
function buildDates(hat, table) {
    numOfWeeks = 1
    const thead = table.querySelector("thead")

    let tr1 = "<tr><th style='border: none;'></th>"
    let tr2 = "<tr><th style='border: none;'></th>"
    let tr3 = "<tr><th style='border: none;'></th>"
    for ([i, month] of hat.entries()) {
        const monthName = MONTHS[month["Month"].split(".")[0]]
        let numWeeks = month["ListOfWeeks"].length
        let klass = (i+1) % 2 == 0 ? "even-month" : "odd-month"
        tr2 += `<th colspan="${numWeeks}" scope="col" class="${klass}">${monthName}</th>`
        for (week of month["ListOfWeeks"]) {
            numOfWeeks += 1
            numOfWeek_evenOdd[numOfWeeks] = klass
            const key = `${week["StartOfTheWeek"]}-${week["EndOfTheWeek"]}`
            tr1 += `<th class="weeks ${klass}"><div class="interval">${key}</div></th>`
            tr3 += `<th scope="col" class="week-counter ${klass}">${week["NumWeek"]}</th>`
            weeks[key] = numOfWeeks
        }
    }
    let tr = `<tr><th style='border: none;'></th><th colspan="${numOfWeeks}" scope="col">2025</th></tr>`
    tr1 += "</tr>"
    tr2 += "</tr>"
    tr3 += "</tr>"
    thead.innerHTML += (tr + tr1 + tr2 + tr3)
}
function buildEmployees(data, tbody) {
    for (division of data) {
        if (division["Data"].length != 0) {

            tbody.innerHTML += `<tr><th class="division" colspan="100" scope='row'><p>${division["selfDivison"]}</p></th></tr>`
            for (employee of division["Data"]) {
                periods = {}
                periodsData = {}
                for (month of employee["ListOfMonth"]) {
                    for (const week of month["ListOfWeeks"]) {
                        const key = `${week["StartOfTheWeek"]}-${week["EndOfTheWeek"]}`;
                        periods[key] = weeks[key];

                        if (!periodsData[weeks[key]]) {
                            periodsData[weeks[key]] = {};
                        }

                        if (week["VacationData"] && week["VacationData"].length > 0) {
                            for (const vacationItem of week["VacationData"]) {
                                for (const _key of Object.keys(vacationItem)) {
                                    if (!periodsData[weeks[key]][_key]) {
                                        periodsData[weeks[key]][_key] = [];
                                    }
                                    periodsData[weeks[key]][_key].push(vacationItem[_key]);
                                }
                            }
                        }
                    }
                }
                let row = `<tr><th class="employee" scope='row'>${employee["Employee"]}</th>`
                for (i = 2; i <= numOfWeeks; i++) {
                    let value = getKeyByValue(periods, i)
                    let classes = "vacation_cell"
                    let numDays = ""
                    let sumDays = 0
                    if (value !== undefined) {
                        let data = periodsData[i]
                        let checkDatesResult;
                        data["Holiday"].forEach((holiday, index) => {
                            if (data["Postponed"][i] === true) {
                                checkDatesResult = filterDatesInPeriod(
                                    data["Holiday"][index],
                                    data["PostponedStartDate"][index],
                                    data["PostponedEndDate"][index]
                                );
                            } else {

                                checkDatesResult = filterDatesInPeriod(
                                    data["Holiday"][index],
                                    data["StartDate"][index],
                                    data["EndDate"][index]
                                );
                            }
                        });

                        if (data["Postponed"].length <= 1) {
                            if (data["Postponed"][0] === true) {
                                numDays = data["PostponedDays"] - checkDatesResult.count
                                classes += " transfered"
                            } else {
                                numDays = data["PlanOfDays"] - checkDatesResult.count
                                classes += " planned"
                            }
                        } else {
                            data["Postponed"].forEach((element, index) => {
                                if (element === true) {
                                    sumDays = sumDays + data["PostponedDays"][index]
                                    numDays = sumDays - checkDatesResult.count
                                } else {
                                    sumDays = sumDays + data["PlanOfDays"][index]
                                    numDays = sumDays - checkDatesResult.count
                                }
                            });
                            classes += " multiply"
                        }
                    }
                    classes += ` ${numOfWeek_evenOdd[i]}`
                    let onClick = ""

                    if (periodsData[i]) {
                        onClick = `onclick='modal(${JSON.stringify(periodsData[i])})'`
                    }
                    row += `<td ${onClick} class="${classes}">${numDays}</td>`
                }
                row += '</tr>'
                tbody.innerHTML += row
            }
        }
        buildEmployees(division["Division"], tbody)
    }
}
function buildTable(data, divisionID) {
    const table = document.querySelector("div#table_step > table")
    buildDates(data["Hat"], table)
    const tbody = table.querySelector("tbody")
    buildEmployees(data["Divisions"], tbody)
    paintingOddMonth();
    hoverColumnPainting()
}
(async () => {
    const DROPDOWNS = document.querySelectorAll(".dropdown");

    const DATE = new Date();
    const CURRENT_YEAR = DATE.getFullYear();
    const START_DATE = `01.01.${CURRENT_YEAR}`
    const END_DATE = `31.12.${CURRENT_YEAR}`
    const URV_HOST = "https://urv.eltex.loc/urv3/hs/person"
    const DIVISIONS_URL = `${URV_HOST}/DivisionVacations?StartDate=${START_DATE}&EndDate=${END_DATE}`
    const VACATIONS_URL = (DivisionID, StartDate = START_DATE, EndDate = END_DATE) => {
        return `${URV_HOST}/ScheduledAnnualVacations?StartDate=${StartDate}&EndDate=${EndDate}&DivisionID=%D0%AD%D0%9B0002618`
    }
    const LOADING_DIV = document.getElementById("loading_step")

    const FORM_STEP_DIV = document.getElementById("form_step")
    const TABLE_STEP_DIV = document.getElementById("table_step")
    const DEPARTMENTS_SELECT = document.getElementById("departments")
    let SELECTED_DIVISION
    let SELECTED_DIVISION_ID
    async function stepTable() {
        document.querySelector("div#table_step > button").addEventListener("click", function() {
            window.location.reload()
        })
        TABLE_STEP_DIV.style.display = "block"
        const resp = await fetch(VACATIONS_URL(SELECTED_DIVISION_ID), {
            method: "GET",
        })
        const data = await resp.json()
        employees = []

        buildTable(data, SELECTED_DIVISION_ID)
    }
    async function stepForm() {
        const resp = await fetch(DIVISIONS_URL, {
            method: "GET",
        })
        if(resp.status !== 200) {
            LOADING_DIV.style.display = "none";
            let errorDIV = document.createElement("div")
            errorDIV.innerHTML = "Вероятно сервис недоступен. Перезагрузите страницу или попробуйте позже";
            document.body.appendChild(errorDIV);
            return false;
        }
        const data = await resp.json()
        divisions = []
        parseDivisions(data)
        FORM_STEP_DIV.style.display = "block"
        LOADING_DIV.style.display = "none"
        for(const division of divisions) {
            const el = document.createElement("option")
            el.value = division["ID"]
            el.text = division["Division"]
            el.classList.add(`lvl-${division["LVL"]}`)
            DEPARTMENTS_SELECT.appendChild(el)
        }
        if (DROPDOWNS.length > 0) {
            DROPDOWNS.forEach((dropdown) => {
                setTimeout(() => {
                    createCustomDropdown(dropdown);
                }, 500);
            });
        }
        document.querySelector("input[type='submit']")?.addEventListener("click", async function(e) {
            const el = document.querySelector("select#departments")
            SELECTED_DIVISION_ID = el?.value
            SELECTED_DIVISION = el?.options[el.selectedIndex].text
            console.log(SELECTED_DIVISION, SELECTED_DIVISION_ID)
            FORM_STEP_DIV.style.display = "none"
            LOADING_DIV.style.display = "flex"
            await stepTable()
            LOADING_DIV.style.display = "none"
        })
    }

    await stepForm()
})();
function filterDatesInPeriod(dates, startDate, endDate) {
    // Функция для преобразования даты
    const parseDate = (dateInput) => {
        if (dateInput instanceof Date) return dateInput;
        if (typeof dateInput === 'string') {
            const [day, month, year] = dateInput.split('.').map(Number);
            return new Date(year, month - 1, day);
        }
        throw new Error('Неподдерживаемый формат даты');
    };
    // Преобразуем границы периода
    const periodStart = parseDate(startDate);
    const periodEnd = parseDate(endDate);
    // Нормализуем входные данные (превращаем в массив)
    const datesArray = Array.isArray(dates) ? dates : [dates];
    // Фильтруем даты
    const datesInPeriod = datesArray.filter(date => {
        try {
            const checkDate = parseDate(date);
            return checkDate >= periodStart && checkDate <= periodEnd;
        } catch (e) {
            console.warn(`Ошибка обработки даты: ${date}`, e);
            return false;
        }
    });
    return {
        count: datesInPeriod.length,
        dates: datesInPeriod.join(", ")
    };
}
function paintingOddMonth() {
    let table = document.querySelector('table');
    let tableHeight = table.scrollHeight;
    let weeksBg = document.querySelectorAll('.weeks .weeks_bg');
    weeksBg.forEach(bg => {
        bg.style.height = tableHeight + 'px';
    });
}
function hoverColumnPainting() {
    const tableRows = document.querySelectorAll('tbody tr');
    let hoverIndex;

    tableRows.forEach(row => {
        const cellsArr = row.querySelectorAll('td.vacation_cell');
        cellsArr.forEach((cell,index) => {
            cell.addEventListener('mouseenter', () => {
                hoverIndex = index;
                painting();
            })
            cell.addEventListener('mouseleave', () => {
                resetPainting();
            });
        });
    });
    function resetPainting() {
        const cellsArr = document.querySelectorAll('td.vacation_cell');

        const weeks = document.querySelectorAll('thead .weeks');
        const weekCounters = document.querySelectorAll('.week-counter');
        cellsArr.forEach(cell => {
            cell.removeAttribute('style');
        });
        weeks.forEach(week => {
            week.removeAttribute('style');
        });
        weekCounters.forEach(counter => {
            counter.removeAttribute('style');
        });
    }
    function painting() {
        tableRows.forEach(row => {
            const hoverColumn = row.querySelectorAll('td')[hoverIndex];
            const weeks = document.querySelectorAll('thead .weeks')[hoverIndex];
            const weekCounters = document.querySelectorAll('.week-counter')[hoverIndex];
            if (hoverColumn) {
                hoverColumn.style.backgroundColor = '#98c1ff';
            }
            weeks.style.background = '#98c1ff';
            weekCounters.style.backgroundColor = '#98c1ff';
        });
    }
}
